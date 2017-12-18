package com.xiahao.lib.structure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DomainStructure {
    public Set<String> apkList;
    public Map<String, Integer> developerCounts;

    public DomainStructure(){
        this.apkList = new HashSet<>();
        this.developerCounts = new HashMap<>();
    }
}
