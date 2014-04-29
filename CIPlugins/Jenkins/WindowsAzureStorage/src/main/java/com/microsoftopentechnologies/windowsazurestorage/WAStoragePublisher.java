/*
 Copyright 2013 Microsoft Open Technologies, Inc.
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.microsoftopentechnologies.windowsazurestorage;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.CopyOnWriteList;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import com.microsoftopentechnologies.windowsazurestorage.beans.StorageAccountInfo;
import com.microsoftopentechnologies.windowsazurestorage.helper.Utils;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Locale;


public class WAStoragePublisher extends Recorder {

	/** Windows Azure Storage Account Name. */
	private  String storageAccName;

	/** Windows Azure storage container name. */
	private  String containerName;

	/** Windows Azure storage container access. */
    private  boolean cntPubAccess;

    /** Files path. Ant glob sysntax. */
    private  String filesPath;
    
    /** File Path prefix  */
    private  String virtualPath;
    
    @DataBoundConstructor
    public WAStoragePublisher(final String storageAccName,final String filesPath,
    						  final	String containerName,final boolean cntPubAccess, final String virtualPath) {
    	super();
    	this.storageAccName	=	storageAccName;
        this.filesPath 		= 	filesPath;
        this.containerName 	= 	containerName;
        this.cntPubAccess 	= 	cntPubAccess;
        this.virtualPath    =   virtualPath;
    }

    public String getFilesPath() {
        return filesPath;
    }

    public String getContainerName() {
        return containerName;
    }

    public boolean isCntPubAccess() {
        return cntPubAccess;
    }

    public String getStorageAccName() {
    	return storageAccName;
    }

    public void setStorageAccName(final String storageAccName) {
		this.storageAccName = storageAccName;
	}

	public void setContainerName(final String containerName) {
		this.containerName = containerName;
	}

	public void setCntPubAccess(final boolean cntPubAccess) {
		this.cntPubAccess = cntPubAccess;
	}

	public void setFilesPath(final String filesPath) {
		this.filesPath = filesPath;
	}
	
	public String getVirtualPath() {
		return virtualPath;
	}

	public void setVirtualPath(final String virtualPath) {
		this.virtualPath = virtualPath;
	}
	
    public WAStorageDescriptor getDescriptor() {
        return (WAStorageDescriptor)super.getDescriptor();
    }
	
	/**
	 * Returns storage account object based on the name selected in job configuration
	 * @return StorageAccount
	 */
	public StorageAccountInfo getStorageAccount() {
		StorageAccountInfo storageAcc = null ;
        for (StorageAccountInfo sa : getDescriptor().getStorageAccounts()) {
            if (sa.getStorageAccName().equals(storageAccName)) {
            	storageAcc = sa;
            	break;
            }
        }
        return storageAcc;
    }

    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) 
    throws InterruptedException, IOException  {
    	
    	// Get storage account and set formatted blob endpoint url. 
    	StorageAccountInfo 	strAcc = getStorageAccount();
    	if (strAcc != null ) {
    		strAcc.setBlobEndPointURL(Utils.getBlobEP(strAcc.getStorageAccName(), strAcc.getBlobEndPointURL()));
    	}
    	
    	// Resolve container name
    	String expContainerName = Utils.replaceTokens(build, listener, containerName);
		if	(expContainerName != null) {
			 expContainerName = expContainerName.trim().toLowerCase(Locale.ENGLISH);
		}
		
		// Resolve file path
		String expFP = Utils.replaceTokens(build, listener, filesPath).trim();
		
		// Resolve virtual path
		String	expVP	= 	Utils.replaceTokens(build, listener, virtualPath);
		if (!Utils.isNullOrEmpty(expVP) && !expVP.endsWith(Utils.FWD_SLASH)) {
			expVP = expVP.trim() + Utils.FWD_SLASH;
		}		
		
        // Validate input data
    	if	(!validateData(build, listener, strAcc, expContainerName)) {
    		return true; // returning true so that build can continue.
    	}

    	try {
        	int filesUploaded = WAStorageClient.upload(build,listener,strAcc,expContainerName,cntPubAccess,expFP,expVP);
    		
    		if (filesUploaded == 0) { //Mark build unstable if no files are uploaded
    			listener.getLogger().println(Messages.WAStoragePublisher_nofiles_uploaded());
    			build.setResult(Result.UNSTABLE);
    		} else {
    			listener.getLogger().println(Messages.WAStoragePublisher_files_uploaded_count(filesUploaded));
    		}
    	} catch(Exception e) {
    		e.printStackTrace(listener.error(Messages.WAStoragePublisher_uploaded_err(strAcc.getStorageAccName())));
    		build.setResult(Result.UNSTABLE);
    	}
    	return true;
    }

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.STEP;
	}
	
	private boolean validateData(AbstractBuild<?, ?> build, BuildListener listener, StorageAccountInfo storageAccount, String expContainerName) 
	throws IOException, InterruptedException {
		
    	// No need to upload artifacts if build failed
    	if (build.getResult() == Result.FAILURE) {
    		listener.getLogger().println(Messages.WAStoragePublisher_build_failed_err());
    		return false;
    	}

    	if (storageAccount == null) {
    		listener.getLogger().println(Messages.WAStoragePublisher_storage_account_err());
    		build.setResult(Result.UNSTABLE);
    		return false;
    	}

    	// Validate container name
    	if (!Utils.validateContainerName(expContainerName)) {
    		listener.getLogger().println(Messages.WAStoragePublisher_container_name_err());
    		build.setResult(Result.UNSTABLE);
    		return false;
    	}

    	// Validate files path
    	if (Utils.isNullOrEmpty(filesPath)) {
    		listener.getLogger().println(Messages.WAStoragePublisher_filepath_err());
    		build.setResult(Result.UNSTABLE);
    		return false;
    	}
    	
    	// Check if storage account credentials are valid
    	try {
			WAStorageClient.validateStorageAccount(storageAccount.getStorageAccName(), storageAccount.getStorageAccountKey(), 
					storageAccount.getBlobEndPointURL());
        } catch (Exception e) {
        	listener.getLogger().println(Messages.Client_SA_val_fail());
        	listener.getLogger().println("Storage Account name --->"+storageAccount.getStorageAccName()+"<----");
        	listener.getLogger().println("Blob end point url --->"+storageAccount.getBlobEndPointURL()+"<----");
        	build.setResult(Result.UNSTABLE);
    		return false;
        }
    	return true;
	}

	@Extension
	public static final class WAStorageDescriptor extends BuildStepDescriptor<Publisher> {

		private final CopyOnWriteList<StorageAccountInfo> storageAccounts = new CopyOnWriteList<StorageAccountInfo>();

		public WAStorageDescriptor() {
			super();
			load();
		}

		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        	storageAccounts.replaceBy(req.bindParametersToList(StorageAccountInfo.class, "was_"));
            save();
            return super.configure(req,formData);
        }

		/**
		 * Validates storage account details.
		 * @param storageAccountName
		 * @param blobEndPointURL
		 * @param storageAccountKey
		 * @return
		 * @throws IOException
		 * @throws ServletException
		 */
		public FormValidation doCheckAccount(@QueryParameter String was_storageAccName, 
							  @QueryParameter String was_storageAccountKey, @QueryParameter String was_blobEndPointURL) 
		throws IOException, ServletException {
			
			if (Utils.isNullOrEmpty(was_storageAccName)) {  
				return FormValidation.error(Messages.WAStoragePublisher_storage_name_req());
			}

			if (Utils.isNullOrEmpty(was_storageAccountKey)) {
					return FormValidation.error(Messages.WAStoragePublisher_storage_key_req());
			}

			try {
				// Get formatted blob end point URL.
				was_blobEndPointURL = Utils.getBlobEP(was_storageAccName, was_blobEndPointURL);
				WAStorageClient.validateStorageAccount(was_storageAccName, was_storageAccountKey, was_blobEndPointURL);
	        } catch (Exception e) {
	            return FormValidation.error("Error : " + e.getMessage());
	        }
			return FormValidation.ok(Messages.WAStoragePublisher_SA_val());
	    }

	/**
	 * Checks for valid container name. 
	 * @param val name of the container
	 * @return FormValidation result 
	 * @throws IOException
	 * @throws ServletException
	 */
		public FormValidation doCheckName(@QueryParameter String val) throws IOException, ServletException {
			if (!Utils.isNullOrEmpty(val)) {
				//Token resolution happens dynamically at runtime , so for basic validations
				// if text contain tokens considering it as valid input.
				if (Utils.containTokens(val) || Utils.validateContainerName(val)) { 
					return FormValidation.ok();
				} else { 
					return FormValidation.error(Messages.WAStoragePublisher_container_name_invalid());
				}
		   } else {
			   return FormValidation.error(Messages.WAStoragePublisher_container_name_req());
		   }
		}

		public FormValidation doCheckPath(@QueryParameter String val) {
			if (Utils.isNullOrEmpty(val) ) { 
				return FormValidation.error(Messages.WAStoragePublisher_artifacts_req());
			}
			return FormValidation.ok();
		}

		@SuppressWarnings("rawtypes")
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		public String getDisplayName() {
			return Messages.WAStoragePublisher_displayName();
		}
		
		public StorageAccountInfo[] getStorageAccounts() {
            return storageAccounts.toArray(new StorageAccountInfo[storageAccounts.size()]);
        }
		
		public String getDefaultBlobURL() {
			return Utils.getDefaultBlobURL();
		}
	}
}

