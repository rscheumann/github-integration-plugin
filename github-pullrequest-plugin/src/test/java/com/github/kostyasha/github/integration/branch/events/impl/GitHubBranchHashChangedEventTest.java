package com.github.kostyasha.github.integration.branch.events.impl;

import com.github.kostyasha.github.integration.branch.GitHubBranch;
import com.github.kostyasha.github.integration.branch.GitHubBranchRepository;
import hudson.model.TaskListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author Kanstantsin Shautsou
 */
@RunWith(MockitoJUnitRunner.class)
public class GitHubBranchHashChangedEventTest {
    @Mock
    private GitHubBranch localBranch;

    @Mock
    private GitHubBranchRepository localRepo;
    @Mock
    private GHRepository repository;

    @Mock
    private GHBranch remoteBranch;
    @Mock
    private TaskListener listener;
    @Mock
    private PrintStream logger;

    private final GitHubBranchHashChangedEvent event = new GitHubBranchHashChangedEvent();


    @Test
    public void branchChanged() throws IOException {
        commonExpectations();
        when(remoteBranch.getSHA1()).thenReturn("57uy57u57u57u57u");

        assertThat(
                event.check(null, remoteBranch, localBranch, localRepo, listener),
                notNullValue()
        );
    }


    @Test
    public void branchCreated() throws IOException {
        commonExpectations();
        assertThat(
                event.check(null, remoteBranch, null, localRepo, listener),
                nullValue()
        );
    }

    @Test
    public void branchNotChangedExisted() throws IOException {
        commonExpectations();
        assertThat(
                event.check(null, remoteBranch, localBranch, localRepo, listener),
                nullValue()
        );
    }


    @Test
    public void branchNotChangedNotExisted() throws IOException {
        commonExpectations();
        assertThat(
                event.check(null, null, null, localRepo, listener),
                nullValue()
        );
    }


    @Test
    public void branchDeleted() throws IOException {
        commonExpectations();
        assertThat(
                event.check(null, null, localBranch, localRepo, listener),
                nullValue()
        );
    }

    private void commonExpectations() throws IOException {
        when(localBranch.getName()).thenReturn("branch-2");
        when(localBranch.getCommitSha()).thenReturn("341r34r134r314r34r");

        when(remoteBranch.getName()).thenReturn("branch-1");
        when(remoteBranch.getOwner()).thenReturn(repository);
        when(remoteBranch.getSHA1()).thenReturn("341r34r134r314r34r");

        when(listener.getLogger()).thenReturn(logger);

        when(repository.getHtmlUrl()).thenReturn(new URL("http://github.com/user/repo/"));
    }
}
