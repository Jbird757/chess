package service;

import dataaccess.MemoryGameDAO;

public class GameService {

    public void clearGameDB() {
        MemoryGameDAO dao = new MemoryGameDAO();
        dao.clearGameDB();
    }
}
