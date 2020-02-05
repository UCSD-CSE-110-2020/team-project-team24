package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.Route;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * provides various methods to help manage Routes
 */
public class RoutesManager {
    private static final String SUCCESSFUL_WRITE = "write: successfully written to file '%s'";
    private static final String SUCCESSFUL_READ = "read: successfully read from file '%s'";

    /**
     * write a list of Route objects to a file
     * @param routes list of routes to be written
     * @param filename file to be written to in app storage
     * @param context application context from which to get file
     * @throws IOException if the file stream could not be created
     */
    public static void writeList(List<Route> routes, String filename, Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(routes);
        oos.close();
        Log.d(RoutesManager.class.getName(), String.format(SUCCESSFUL_WRITE, filename));
    }

    /**
     * read a list of Route objects from a file
     * @param filename file to be read from in app storage
     * @param context application context from which to get file
     * @return a List<Route> object
     * @throws IOException if the file stream could not be created
     */
    public static List<Route> readList(String filename, Context context) throws IOException {
        ObjectInputStream ois = getInputStream(filename, context);
        if (ois == null) {
            Log.e(RoutesManager.class.getName(), String.format(SUCCESSFUL_READ, filename));
            return new ArrayList<>();
        }
        List<Route> routes = null;
        try {
            routes = (List<Route>) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
        return (routes == null) ? new ArrayList<>() : routes;
    }

    // for convenience - gets input stream, handling exceptions
    private static ObjectInputStream getInputStream(String filename, Context context) throws FileNotFoundException {
        FileInputStream fis = context.openFileInput(filename);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ois;
    }
}

//rivate void printRoute(Route route) {
//        System.out.println("Title: " + route.getTitle());
//        if (route.getStartingLocation() != null)
//        System.out.println(route.getStartingLocation());
//        }
//protected void writeRoutes(List<Route> routes, Context context) {
//        try (FileOutputStream fos = context.openFileOutput(STORAGE_FILE, Context.MODE_PRIVATE)) {
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(routes);
//        Log.d("writing", "writeRoute: routes written to file");
//        } catch (FileNotFoundException e) {
//        e.printStackTrace();
//        } catch (IOException e) {
//        e.printStackTrace();
//        }
//        }
//protected List<Route> readRoutes(Context context) {
//        try (FileInputStream fis = context.openFileInput(STORAGE_FILE)){
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        Log.d("reading", "readRoutes: reading routes to list");
//        return (List<Route>) ois.readObject();
//        } catch (FileNotFoundException e) {
//        Log.e("reading", "readRoutes: file hasn't been created");
//        e.printStackTrace();
//        } catch (IOException e) {
//        e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//        e.printStackTrace();
//        }
//        return new ArrayList<>();
//        }