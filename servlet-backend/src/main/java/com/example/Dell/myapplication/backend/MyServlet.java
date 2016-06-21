/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.Dell.myapplication.backend;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import java.io.File;


public class MyServlet extends HttpServlet {
    /** Application name. */
    private static final String APPLICATION_NAME =
            "Drive API Java Quickstart";

    /** Directory to store user credentials for this application. */
//    private static final java.io.File DATA_STORE_DIR = new java.io.File(            System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
//    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart.json
     */
   private static final List<String> SCOPES =
            Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
  //          DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Please use the form to POST to this url");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String name = req.getParameter("name");
        resp.setContentType("text/plain");
        if (name == null) {
            resp.getWriter().println("Please enter a name");
        }
        resp.getWriter().println("Hello " + name);
        // Build a new authorized API client service.
        Drive service = getDriveService();
        printFile( service);

    }
    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public  Credential authorize() throws IOException {
        // Load client secrets.
   /*     InputStream in =
                DriveQuickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
     */// setServiceAccountId(IRingeeConstants.SERVICE_ACCOUNT_EMAIL)
        URL resource = getServletContext().getResource("/WEB-INF/Google cloud app-7e0287a68575.p12");
       // URL resource = getServletContext().getResource("/WEB-INF/rups.txt");
        java.io.File file=null;
        try {
             file =  new java.io.File(resource.toURI());
        /*    try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    System.out.println(sCurrentLine);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
*/

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        GoogleCredential credential = null;
        try {
            credential = new GoogleCredential
                    .Builder()
                    .setTransport(HTTP_TRANSPORT)
                    .setJsonFactory(JSON_FACTORY)

                    .setServiceAccountId("perfect-entry-134823@appspot.gserviceaccount.com")
                    .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))//DriveScopes.DRIVE_APPDATA
                    .setServiceAccountPrivateKeyFromP12File(file)
                    .build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

      //  System.out.println(                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }
    public  Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
             //   .setApplicationName(APPLICATION_NAME)
                .build();
    }
 void printFile(Drive service){
        // Print the names and IDs for up to 10 files.
        FileList result = null;
        try {
            result = service.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, files(id, name)")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<File> files = result.getFiles();
        if (files == null || files.size() == 0) {
            System.out.println("No files found.");
        } else {
            System.out.println("Files:");
            for (File file : files) {
                System.out.printf("%s (%s)\n", file.getName(), file.getId());
            }
        }
    }
   /* void perm(Drive service) {
   File body = new File();
  //  body.setTitle("ringee");
    body.setDescription("ringeeapp");
    body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
    body.setMimeType("application/vnd.google-apps.folder");
    java.io.File fileContent = new java.io.File("G:\\document.txt");
    FileContent mediaContent = new FileContent("text/plain", fileContent);

    File file = service.files().insert(body, mediaContent).execute();
    System.out.print("file id is :" + file.getId());
    Permission newPermission = new Permission();
    // for showing files in browser that reason only using additional
    // permission
    newPermission.setValue(IRingeeConstants.USER_ACCOUNT_EMAIL);
    newPermission.setType("owner");
    newPermission.setRole("writer");
    service.permissions().insert(file.getId(), newPermission).execute();
    getFileByFileId(service, file.getId());

    }*/
   /* void driveauthen() {
       HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        try {
            GoogleCredential credential = new GoogleCredential.Builder().
                    setTransport(httpTransport).
                    setJsonFactory(jsonFactory).
                    setServiceAccountId(IRingeeConstants.SERVICE_ACCOUNT_EMAIL)
                    .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE_APPDATA)).
                            setServiceAccountPrivateKeyFromP12File(new java.io.File("G:\\Ringee-1a1f1b786226.p12")).build();
            new Drive.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
          //  Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName(IRingeeConstants.APPLICATION_NAME).build();

            File body = new File();
            body.setTitle("ringee");
            body.setDescription("ringeeapp");
            body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
            body.setMimeType("application/vnd.google-apps.folder");
            java.io.File fileContent = new java.io.File("G:\\document.txt");
            FileContent mediaContent = new FileContent("text/plain", fileContent);

            File file = service.files().insert(body, mediaContent).execute();
            System.out.print("file id is :" + file.getId());
            Permission newPermission = new Permission();
            // for showing files in browser that reason only using additional
            // permission
            newPermission.setValue(IRingeeConstants.USER_ACCOUNT_EMAIL);
            newPermission.setType("owner");
            newPermission.setRole("writer");
            service.permissions().insert(file.getId(), newPermission).execute();
            getFileByFileId(service, file.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
