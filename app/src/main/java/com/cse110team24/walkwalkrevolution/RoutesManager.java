package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.Route;

import java.io.File;
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
    private static final String TAG = "RoutesManager";

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
        Log.i(TAG, "writeList: sucessfully wrote list of routes to " + filename);
    }

    /**
     * write a Route object to a file
     * @param route Route object to be written
     * @param filename file to be written to in app storage
     * @param context application context from which to get file
     * @throws IOException if the file stream could not be created
     */
    public static void writeSingle(Route route, String filename, Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(route);
        oos.close();
        Log.i(TAG, "writeSingle: successfully wrote single Route object to " + filename);
    }

    /**
     * read a list of Route objects from a file
     * @param filename file to be read from in app storage
     * @param context application context from which to get file
     * @return a List<Route> object (empty if file was not read)
     * @throws IOException if the file stream could not be created
     */
    public static List<Route> readList(String filename, Context context) throws IOException {
        ObjectInputStream ois = getInputStream(filename, context);
        if (ois == null) {
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

    /**
     * read a route object from a file
     * @param filename file to be read from in app storage
     * @param context application context from which to get file
     * @return a Route object (caution: returns null if not read)
     * @throws IOException if the file stream could not be created
     */
    public static Route readSingle(String filename, Context context) throws IOException {
        ObjectInputStream ois = getInputStream(filename, context);
        if (ois == null) {
            return null;
        }

        Route route = null;
        try {
            route = (Route) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
        return route;
    }

    /**
     * @param filename filename to look for latest route
     * @return a route object if a latest route exists or null otherwise
     */
    public static Route readLatest(String filename, Context context) {
        Route latest = null;
        try {
            latest = readSingle(filename, context);
        } catch (IOException e) {}

        return latest;
    }

    /**
     * otherwise just write the new route. if route's stats are null, does nothing
     * @param route route to be written to file
     * @param filename file to be written to
     */
    public static void writeLatest(Route route, String filename, Context context) throws IllegalArgumentException,
                                                                                    IOException {
        if (route.getStats() == null) {
            throw new IllegalArgumentException("Can't write latest route without stats");
        }
        deleteExistingFile(filename);
        writeSingle(route, filename, context);
    }

    private static void deleteExistingFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
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