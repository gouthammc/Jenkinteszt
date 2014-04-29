/**
 * Copyright 2013 Persistent Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.interopbridges.tools.windowsazure;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class WindowsAzureEndpointSATest {

    private WindowsAzureProjectManager wProj = null;
    private WindowsAzureRole role = null;
    @Before
    public final void setUp() {
        try {
            wProj = WindowsAzureProjectManager.load(
                    new File(Messages.getString("WinAzureTestConstants.SAWindowsAzureProj")));
            role = wProj.getRoles().get(0);

        } catch (WindowsAzureInvalidProjectOperationException e) {
            e.printStackTrace();
            fail("test case failed");
        }
    }

    @Test
    public void testConfigureSessionAffinity()
    throws WindowsAzureInvalidProjectOperationException {
        WindowsAzureEndpoint windowsAzureEndpoint = role.getEndpoint("http") ;
        role.setSessionAffinityInputEndpoint(windowsAzureEndpoint) ;
        WindowsAzureEndpoint saEndPt = role.getSessionAffinityInputEndpoint() ;
        assertEquals(windowsAzureEndpoint.getName(),saEndPt.getName());
        wProj.save();
    }
    
    @Test
    public void testConfigureSslProxy()
    throws WindowsAzureInvalidProjectOperationException {
        WindowsAzureEndpoint windowsAzureEndpoint = role.getEndpoint("http") ;
        
        
        WindowsAzureCertificate cert = new WindowsAzureCertificate("Azure tools 2", "E7A1173C9A56791633EB9B49B45409D757BE92BF");
        role.setSslOffloading(windowsAzureEndpoint, cert);
       
        WindowsAzureEndpoint saEndPt = role.getSslOffloadingInputEndpoint() ;
        assertEquals(windowsAzureEndpoint.getName(), saEndPt.getName());
        wProj.save();
    }
    
    @Test
    public void testGetSslCert()
    throws WindowsAzureInvalidProjectOperationException {
        WindowsAzureEndpoint windowsAzureEndpoint = role.getEndpoint("http2") ; 
        WindowsAzureCertificate cert = new WindowsAzureCertificate("Azure tools 2", "23545644");
        
        role.setSslOffloading(windowsAzureEndpoint, cert);
       
        wProj.save();
        
        WindowsAzureCertificate newCert =  role.getSslOffloadingCert();
        assertEquals(cert.getName(), newCert.getName());
        assertEquals(cert.getFingerPrint(), newCert.getFingerPrint());
    }
    
    @Test
    public void tesSetSslCert()
    throws WindowsAzureInvalidProjectOperationException {
        WindowsAzureCertificate cert = new WindowsAzureCertificate("Azure tools 3", "11111111");
        
        role.setSslOffloadingCert(cert);
       
        wProj.save();
        
        WindowsAzureCertificate newCert =  role.getSslOffloadingCert();
        assertEquals(cert.getName(), newCert.getName());
        assertEquals(cert.getFingerPrint(), newCert.getFingerPrint());
    }
    
    
    @Test
    public void testDisableSslProxy()
    throws WindowsAzureInvalidProjectOperationException {
        role.setSslOffloading(null, null);
       
        wProj.save();
        WindowsAzureEndpoint saEndPt = role.getSslOffloadingInputEndpoint() ;
        assertNull(saEndPt);
    }




    @Test
    public void testGetSessionAffinityInputEndPoint()
    throws WindowsAzureInvalidProjectOperationException {

        WindowsAzureEndpoint saEndPt =  role.getSessionAffinityInputEndpoint() ;
        assertEquals("http1",saEndPt.getName());
    }

    @Test
    public void testGetSessionAffinityInternalEndPoint()
    throws WindowsAzureInvalidProjectOperationException {
        WindowsAzureEndpoint windowsAzureEndpoint = role.getSessionAffinityInternalEndpoint() ;
        assertEquals("http1_SESSION_AFFINITY",windowsAzureEndpoint.getName());
    }

    @Test
    public void testSetInputEndPointName()
    throws WindowsAzureInvalidProjectOperationException {
           WindowsAzureEndpoint windowsAzureEndpoint = role.getEndpoint("http1") ;
           windowsAzureEndpoint.setName("http2") ;
           WindowsAzureEndpoint saEndPt = role.getSessionAffinityInputEndpoint() ;
           assertEquals(windowsAzureEndpoint.getName(),saEndPt.getName());
     }

    @Test
    public void testSetInternalEndPointName()
    throws WindowsAzureInvalidProjectOperationException {
           WindowsAzureEndpoint windowsAzureEndpoint = role.getEndpoint("http1_SESSION_AFFINITY") ;
           windowsAzureEndpoint.setName("http2_SESSION_AFFINITY") ;
           assertEquals("http2_SESSION_AFFINITY",windowsAzureEndpoint.getName());
     }

    @Test
    public void testDisableSessionAffinity()
    throws WindowsAzureInvalidProjectOperationException {
         role.setSessionAffinityInputEndpoint(null);
         assertNull(role.getSessionAffinityInputEndpoint());
         wProj.save();
     }

     @Test
     public void testChangeRoleName()
     throws WindowsAzureInvalidProjectOperationException {
         role.setName("newworkerrole");
         assertEquals("newworkerrole",role.getName());
      }
}
