package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class MemoryAuthDAO implements AuthDAO {
    private static List<AuthData> auths = new ArrayList<>();
    
    @Override
    public void clearAuthDB() {
        auths.clear();
    }
}
