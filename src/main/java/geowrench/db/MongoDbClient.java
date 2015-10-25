package geowrench.db;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import geowrench.UserRole;
import geowrench.logger.LoggerProvider;
import java.io.File;
import java.io.IOException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tero Rantanen
 */
@ManagedBean
@ApplicationScoped
public class MongoDbClient {
    
    private final MongoClient mongoClient = new MongoClient();
    private final MongoDatabase db = mongoClient.getDatabase("geowrench");
    private static MongoDbClient instance;
    
    Logger log = LoggerProvider.getInstance().getLogger();
    
    // Private constructor, jotta luokasta ei voi luoda omaa instanssia
    // ilman että kutsuu getInstance() -metodia.
    private MongoDbClient(){}
    
    public static synchronized MongoDbClient getInstance() {
        if (instance == null) {
            instance = new MongoDbClient();
        }
        return instance;
    }
    
    public synchronized void queryDocuments() {
        System.out.println("DB HASHCODE= " + db.hashCode());
        try (MongoCursor<Document> cursor = db.getCollection("restaurants").find().iterator()) {
            while (cursor.hasNext()) {
                //System.out.println(cursor.next().toJson());
            }
        }
    }
    
    public synchronized void insertDocument(String json) {
        try {
            db.getCollection("routes").insertOne(new Document("key1", json));
        } catch (MongoException e) {            
            log.error("Document insert failed: " , e);
        }
    }

    public synchronized void insertGPXFile(File gpxfile, String username) {
        try {
            MongoCollection<Document> collection = db.getCollection("gpxfiles_meta");
            DB gridfsDB = mongoClient.getDB("geowrench");
            GridFS gfs = new GridFS(gridfsDB, "gpxfiles");
            GridFSInputFile gfsInput = gfs.createFile(gpxfile);
            gfsInput.setFilename(gpxfile.getName());
            // store the file to MongoDB using GridFS
            gfsInput.save();
            
            Document document = new Document("user", username).append("filename", gpxfile.getName());
            // store the gpxfile metadata to MongoDB
            
            Document alreadyThere = collection.find(eq("filename", gpxfile.getName())).first();
            if (alreadyThere == null) {
                collection.insertOne(document);            
            } else {
                System.out.println("Already there - not stored to DB.");
            }          
        } catch (IOException ex) {
            ex.printStackTrace();
            //Logger.getLogger(MongoDbClient.class.getName()).log(Level.SEVERE, null, ex);
            //FacesMessageUtil.addFacesMessage(localeBean.getLocalizedString("mongodb_gpxfile_insert_error"), FacesMessage.SEVERITY_ERROR);            
        }
    }
    
    public synchronized void createUserCredentials(String username, String pwd, UserRole role) throws MongoException {
        MongoCollection<Document> collection = db.getCollection("user_credentials");
        Document document = new Document("username", username).append("pwd", pwd).append("role", role.getValue());

        Document alreadyThere = collection.find(eq("username", username)).first();
        if (alreadyThere == null) {
            collection.insertOne(document);
        } else {
            System.out.println("Already there - not stored to DB.");
            throw new MongoException("Document already exists.");
        }
    }
    
    public synchronized void updateUserCredentials(String username, String pwd) throws MongoException {
        log.debug("Updating credentials for user '" + username + "'");
        MongoCollection<Document> collection = db.getCollection("user_credentials");
        collection.updateOne(new Document("username", username), new Document("$set", new Document("pwd", pwd)));
        
        //db.getCollection("restaurants").updateOne(new Document("name", "Juni"),
        //new Document("$set", new Document("cuisine", "American (New)"))
        //    .append("$currentDate", new Document("lastModified", true)));
        
    }
    
    public synchronized String queryUserCredentials(String username) throws MongoException {
        String pwd = "";
        try {
            MongoCollection<Document> collection = db.getCollection("user_credentials");
            Document credentials = collection.find(eq("username", username)).first();
            if (credentials != null) {
                String json = credentials.toJson();
                JSONObject obj = new JSONObject(json);
                pwd = obj.getString("pwd");                
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return pwd;
    }
    
    public synchronized UserRole queryUserRole(String username) throws MongoException {
        UserRole role = null;
        try {
            MongoCollection<Document> collection = db.getCollection("user_credentials");
            Document credentials = collection.find(eq("username", username)).first();
            if (credentials != null) {
                String json = credentials.toJson();
                JSONObject obj = new JSONObject(json);
                int roleValue = obj.getInt("role");
                role = UserRole.values()[roleValue];
            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
        System.out.println("UserRole from DB: " + role);
        return role;
    }
}
