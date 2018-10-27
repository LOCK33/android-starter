package net.bndy.ad.framework;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import net.bndy.ad.R;
import net.bndy.ad.framework.exception.UnsupportedResourceTypeException;

import java.util.ArrayList;
import java.util.List;

public class ResourceInfo {

    private int id;
    private String name;
    private String type;
    private Context context;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResourceInfo(Context context, int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.context = context;
    }


    public Object tryGet(Object... args) {
        try {
            return get(args);
        } catch (UnsupportedResourceTypeException ex) {
            ex.printStackTrace();
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Object get(Object... args) {
        switch (type) {
            case "id":
                return ((Activity)context).findViewById(id);

            case "drawable":
                return ApplicationUtils.getDrawable(id, context);

            case "string":
                return context.getResources().getString(id, args);

            case "array":
                return context.getResources().getStringArray(id);

            case "plurals":
                List<Object> lst = new ArrayList<>();
                for(int i = 1; i < args.length; i++) {
                    lst.add(args[i]);
                }
                return context.getResources().getQuantityString(id, (int) args[0], lst.toArray());
        }

        throw new UnsupportedResourceTypeException(id, type);
    }
}
