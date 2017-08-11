package com.hankz.util.dbService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LibsDbServiceTest {
    LibsDbService libsDbService;
    @Before
    public void setUp() throws Exception {
        libsDbService = new LibsDbService();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setCacheSwitch() throws Exception {
        libsDbService.setCacheSwitch(false);
    }

    @Test
    public void loadAllData() throws Exception {
        libsDbService.loadAllData(false);
    }

    @Test
    public void insertLibraryInfo() throws Exception {
    }

    @Test
    public void insertLibraryInfoList() throws Exception {
    }

    @Test
    public void getLibraryList() throws Exception {
    }

    @Test
    public void getLibraryFingerprint() throws Exception {
    }

    @Test
    public void getLibraryConstants() throws Exception {
    }

    @Test
    public void searchLibsByFingerprint() throws Exception {
    }

}