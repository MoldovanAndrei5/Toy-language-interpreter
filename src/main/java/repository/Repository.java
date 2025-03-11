package repository;

import exceptions.RepositoryException;
import model.programState.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Repository implements IRepository {
    private String logFilePath;
    List<PrgState> prgList;

    public Repository(List<PrgState> prgList, String logFilePath) {
        this.prgList = prgList;
        this.logFilePath = logFilePath;
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgList;
    }

    @Override
    public void setPrgList(List<PrgState> plist) {
        prgList = plist;
    }

    @Override
    public void logPrgStateExec(PrgState prgState) throws RepositoryException {
        PrintWriter logFile;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
        }
        catch (IOException e) {
            throw new RepositoryException("There was an error while writing to the log file.");
        }
        logFile.append(prgState.toString());
        logFile.close();
    }

    @Override
    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }
}
