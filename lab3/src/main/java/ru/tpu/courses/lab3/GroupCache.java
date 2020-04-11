package ru.tpu.courses.lab3;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class GroupCache {
    private static GroupCache instance;

    static GroupCache getInstance() {
        if (instance == null) {
            synchronized (GroupCache.class) {
                if (instance == null) {
                    instance = new GroupCache();
                    instance.groups.add(new Group("first group"));
                    instance.groups.add(new Group("second group"));
                    instance.groups.add(new Group("third group"));
                }
            }
        }
        return instance;
    }

    private Set<Group> groups = new LinkedHashSet<>();

    Group getGroupByName(String name) {
        for (Group g : groups) {
            if (name.equals(g.groupName))
                return g;
        }
        return null;
    }

    @NonNull
    List<Group> getGroups() {

        return new ArrayList<>(groups);

    }

    void addGroup(@NonNull Group group) {
        groups.add(group);
    }
}
