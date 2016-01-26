package com.aosijia.dragonbutler.imagegroup.model;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageFolder implements Serializable {

    public String dir;
    public String firstImagePath;
    public String name;
    public int count;
    public List<Image> images;

    public ImageFolder() {
        images = new ArrayList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageFolder that = (ImageFolder) o;

        if (dir != null ? !dir.equals(that.dir) : that.dir != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = dir != null ? dir.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
