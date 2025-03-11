package repository;

import exceptions.RepositoryException;
import model.programState.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepository {
    void logPrgStateExec(PrgState prgState) throws RepositoryException;
    void setLogFilePath(String logFilePath);
    List<PrgState> getPrgList();
    void setPrgList(List<PrgState> prgList);
}
