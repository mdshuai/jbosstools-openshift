/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.test.core.connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jboss.tools.openshift.core.connection.Connection;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.openshift3.client.IClient;
import com.openshift3.client.ResourceKind;
import com.openshift3.client.model.IProject;
import com.openshift3.client.model.IResource;

/**
 * @author Andre Dietisheim
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionTest {
	
	private Connection connection;
	@Mock IClient client;
	
	@Before
	public void setup() throws Exception{
		when(client.getBaseURL()).thenReturn(new URL("https://localhost:8433"));
		connection = new Connection(client, null, null, null);
	}
	@Test
	public void getResourceKindShouldCallClient(){
		List<IProject> projects = Arrays.asList(mock(IProject.class));
		when(client.<IProject>list(ResourceKind.Project)).thenReturn(projects);
		
		assertArrayEquals("Exp. to get projects from the client",projects.toArray(), connection.get(ResourceKind.Project).toArray());
	}
	@Test
	public void testGetHost() {
		assertEquals("localhost", connection.getHost());
	}
	@Test
	public void testGetScheme() {
		assertEquals("https", connection.getScheme());
	}
	
	@Test
	public void testNotEquals() throws Exception{
		Connection one = new Connection("https://localhost:8443", null, null, null);
		Connection two = new Connection("https://localhost:8443", null, null, null);
		two.setUsername("foo");
		
		assertNotEquals("Exp. connections not to be equal unless they have same url and user", one,  two);
	}

	@Test
	public void testEquals() throws Exception{
		Connection one = new Connection("https://localhost:8443", null, null, null);
		one.setUsername("foo");
		Connection two = new Connection("https://localhost:8443", null, null, null);
		two.setUsername("foo");
		
		assertEquals("Exp. connections to be equal if they have same url and user", one,  two);
	}
	
//	@Test
//	public void nullHostShouldBeDefaultHost() {
//		// pre-conditions
//
//		// operations
//		Connection connection = new ConnectionFake("fakeUser", null);
//
//		// verifications
//		assertTrue(connection.isDefaultHost());
//		assertEquals(ConnectionUtils.getDefaultHostUrl(), connection.getHost());
//	}
//
//	@Test
//	public void manuallySetDefaultHostShouldNotBeDefaultHost() {
//		// pre-conditions
//		String host = ConnectionUtils.getDefaultHostUrl();
//		assertNotNull(host);
//
//		// operations
//		Connection connection = new ConnectionFake("fakeUser", host);
//
//		// verifications
//		assertFalse(connection.isDefaultHost());
//		assertEquals(ConnectionUtils.getDefaultHostUrl(), connection.getHost());
//	}
//
//	@Test
//	public void setHostShouldResetIsDefaultHost() {
//		// pre-conditions
//		Connection connection = new ConnectionFake("fakeUser", null);
//		assertTrue(connection.isDefaultHost());
//
//		// operations
//		connection.setHost("http://www.redhat.com");
//
//		// verifications
//		assertFalse(connection.isDefaultHost());
//	}
//
//	@Test
//	public void shouldExtractUrlPortions() throws UnsupportedEncodingException, MalformedURLException {
//		// pre-conditions
//		String scheme = UrlUtils.SCHEME_HTTP;
//		String username = "adietish@redhat.com";
//		String password = "12345";
//		String server = "openshift.redhat.com";
//
//		// operations
//		ConnectionURL connectionUrl = ConnectionURL.forURL(scheme + URLEncoder.encode(username, "UTF-8") + ":"
//				+ password + "@" + server);
//		Connection connection = new ConnectionFake(connectionUrl.getUsername(), connectionUrl.getScheme(), connectionUrl.getHost());
//
//		// verifications
//		assertEquals(scheme, connection.getScheme());
//		assertEquals(username, connection.getUsername());
//		assertEquals(scheme + server, connection.getHost());
//	}
//
//	@Test
//	public void shouldAllowPortInUrl() throws UnsupportedEncodingException, MalformedURLException {
//		// pre-conditions
//
//		// operations
//		ConnectionURL connectionUrl = ConnectionURL.forURL("http://adietish%40redhat.com@localhost:8081");
//		Connection connection = new ConnectionFake(connectionUrl.getUsername(), connectionUrl.getScheme(), connectionUrl.getHost());
//
//		// verifications
//		assertEquals("http://localhost:8081", connection.getHost());
//	}
//
//	@Test
//	public void shouldHaveHostWithScheme() {
//		// pre-conditions
//
//		// operations
//		Connection connection = new ConnectionFake("fakeUser", "openshift.redhat.com");
//
//		// verifications
//		assertNotNull(connection.getHost());
//		assertTrue(connection.getHost().startsWith(UrlUtils.HTTP));
//		assertNotNull(connection.getScheme());
//		assertTrue(connection.getScheme().startsWith(UrlUtils.HTTP));
//	}
//
//	@Test
//	public void shouldHaveHostWithSchemeAfterSetting() {
//		// pre-conditions
//		Connection connection = new ConnectionFake("fakeUser", "openshift.redhat.com");
//
//		// operations
//		connection.setHost("jboss.com");
//
//		// verifications
//		assertNotNull(connection.getHost());
//		assertTrue(connection.getHost().startsWith(UrlUtils.HTTP));
//		assertNotNull(connection.getScheme());
//		assertTrue(connection.getScheme().startsWith(UrlUtils.HTTP));
//	}
//
//	@Test
//	public void shouldNotOverrideGivenScheme() {
//		// pre-conditions
//
//		// operations
//		Connection connection = new ConnectionFake("fakeUser", "scheme://openshift.redhat.com");
//
//		// verifications
//		assertNotNull(connection.getHost());
//		assertTrue(connection.getHost().startsWith("scheme://"));
//		assertNotNull(connection.getScheme());
//		assertEquals("scheme://", connection.getScheme());
//	}
//
//	@Test
//	public void setUsernameShouldDisconnect() {
//		// pre-conditions
//		ConnectionFake connection = new ConnectionFake("fakeUser", "openshift.redhat.com");
//		connection.setConnected(true);
//		assertTrue(connection.isConnected());
//
//		// operations
//		connection.setUsername("adietish");
//
//		// verifications
//		assertFalse(connection.isConnected());
//	}
//
//	@Test
//	public void setPasswordShouldDisconnect() {
//		// pre-conditions
//		ConnectionFake connection = new ConnectionFake("fakeUser", "openshift.redhat.com");
//		connection.setConnected(true);
//		assertTrue(connection.isConnected());
//
//		// operations
//		connection.setPassword("fakePassword");
//
//		// verifications
//		assertFalse(connection.isConnected());
//	}
//
//	@Test
//	public void setHostShouldDisconnect() {
//		// pre-conditions
//		ConnectionFake connection = new ConnectionFake("fakeUser", "openshift.redhat.com");
//		connection.setConnected(true);
//		assertTrue(connection.isConnected());
//
//		// operations
//		connection.setHost("fakeHost");
//
//		// verifications
//		assertFalse(connection.isConnected());
//	}
//
//	@Test
//	public void shouldUpdate() {
//		// pre-conditions
//		ConnectionFake connection = new ConnectionFake("fakeUser", null);
//		connection.setRememberPassword(true);
//		connection.setConnected(true);
//		assertTrue(connection.isConnected());
//		assertTrue(connection.isDefaultHost());
//
//		String newUsername = "anotherUser";
//		String newHost = "http://www.redhat.com";
//		String newPassword = "1q2w3e";
//		Connection updatingConnection = new ConnectionFake(newUsername, newHost);
//		updatingConnection.setPassword(newPassword);
//		updatingConnection.setRememberPassword(false);
//
//		// operations
//		connection.update(updatingConnection);
//
//		// verifications
//		assertEquals(newUsername, connection.getUsername());
//		assertEquals(newHost, connection.getHost());
//		assertFalse(newUsername, connection.isDefaultHost());
//		assertEquals(newPassword, connection.getPassword());
//		assertFalse(connection.isRememberPassword());
//		assertFalse(connection.isConnected());
//	}
}
