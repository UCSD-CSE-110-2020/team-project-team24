package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.route.Route;

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
        Log.i(TAG, "writeList: successfully wrote list of routes to " + filename);
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

    public static void appendToList(Route route, String filename, Context context) throws IOException {
        List<Route> storedRoutes = readList(filename, context);
        storedRoutes.add(route);
        writeList(storedRoutes, filename, context);
        Log.i(TAG, "appendToList: successfully appended single Route object to" + filename +" by calling writeListg");
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

    public static void replaceInList(Route route, int idx, String listFilename, Context context) throws IOException{
        if (idx < 0) {
            appendToList(route, listFilename, context);
            return;
        }
        List<Route> routes = readList(listFilename, context);
        routes.remove(idx);
        routes.add(idx, route);
        writeList(routes, listFilename, context);
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
        deleteExistingFile(filename, context);
        writeSingle(route, filename, context);
    }

    private static void deleteExistingFile(String filename, Context context) {
        context.deleteFile(filename);
    }

    // for convenience - gets input stream, handling exceptions
    private static ObjectInputStream getInputStream(String filename, Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(fis);
        } catch (IOException e) {}
        return ois;
    }

    public static class AsyncTaskSaveRoutes extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            List<Route> routes = (List<Route>) params[0];
            Context context = (Context) params[1];
            try {
                writeList(routes, RoutesActivity.LIST_SAVE_FILE, context);
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: Couldn't save to file", e);
                return null;
            }
            Log.i(TAG, "doInBackground: saved current instance of routes to file");
            return null;
        }
    }
}