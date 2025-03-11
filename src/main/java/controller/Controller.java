package controller;

import exceptions.ADTException;
import exceptions.ControllerException;
import exceptions.PrgStateException;
import exceptions.StatementException;
import model.programState.MyStack.MyIStack;
import model.programState.PrgState;
import model.statements.IStmt;
import model.values.RefValue;
import model.values.Value;
import repository.IRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    IRepository repo;
    String displayFlag = "on";
    ExecutorService executor;

    public Controller(IRepository repo) {
        this.repo = repo;
    }

    public List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream().filter(v->v instanceof RefValue).map(v-> {RefValue v1 = (RefValue) v; return v1.getAddr();}).collect(Collectors.toList());
    }

    public List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream().filter(v->v instanceof RefValue).map(v-> {RefValue v1 = (RefValue) v; return v1.getHeapAddress();}).collect(Collectors.toList());
    }

    public Map<Integer, Value> unsafeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e->symTableAddresses.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /*public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, Value> heap) {
        List<Integer> addresses = new ArrayList<>(symTableAddresses);

        boolean changed;
        do {
            List<Integer> newAddresses = heap.entrySet().stream()
                    .filter(entry -> addresses.contains(entry.getKey()))
                    .map(entry -> {
                        if (entry.getValue() instanceof RefValue) {
                            return ((RefValue) entry.getValue()).getAddr();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            changed = addresses.addAll(newAddresses.stream()
                    .filter(address -> !addresses.contains(address))
                    .toList());
        } while (changed);

        return unsafeGarbageCollector(addresses, heap);
    }*/

    public static List<Integer> getAddressesFromSymTableAndHeap(Collection<Value> symTableValues, Collection<Value> heapValues) {
        List<Integer> symTableAddresses = symTableValues.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).getAddr())
                .collect(Collectors.toList());

        List<Integer> heapAddresses =  heapValues.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).getAddr())
                .collect(Collectors.toList());

        symTableAddresses.addAll(heapAddresses);
        return symTableAddresses;
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddresses, List<Integer> heapAddresses, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> ( symTableAddresses.contains(e.getKey()) || heapAddresses.contains(e.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void conservativeGarbageCollector(List<PrgState> programStates) {
        List<Integer> symTableAddresses = Objects.requireNonNull(programStates.stream()
                        .map(p -> getAddrFromSymTable(p.getSymTable().getContent().values()))
                        .map(Collection::stream)
                        .reduce(Stream::concat).orElse(null))
                .collect(Collectors.toList());
        programStates.forEach(p -> p.getHeapTable().setContent((HashMap<Integer, Value>) safeGarbageCollector(symTableAddresses, getAddrFromHeap(p.getHeapTable().getContent().values()), p.getHeapTable().getContent())));
    }

    public void oneStep() throws StatementException, InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        oneStepForAllPrg(prgList);
        conservativeGarbageCollector(prgList);
        //prgList = removeCompletedPrg(repo.getPrgList());
        executor.shutdownNow();
        //repo.setPrgList(prgList);
    }

    public void allStep() throws PrgStateException, ControllerException, StatementException, ADTException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while(prgList.size() > 0) {
            conservativeGarbageCollector(prgList);
            //PrgState state = prgList.get(0);
            //state.getHeapTable().setContent(safeGarbageCollector(getAddrFromSymTable(state.getSymTable().getContent().values()), state.getHeapTable().getContent()));
            try {
                oneStepForAllPrg(prgList);
                prgList = removeCompletedPrg(repo.getPrgList());
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        prgList.forEach(prg->repo.logPrgStateExec(prg));
        List<Callable<PrgState>> callList = prgList.stream().map((PrgState p) -> (Callable<PrgState>)(() -> {return p.oneStep();})).collect(Collectors.toList());
        // start the execution of the callables
        List<PrgState> newPrgList = executor.invokeAll(callList).stream().map(future -> {
            try {
                return future.get();
            } catch(InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }).filter(p -> p != null).collect(Collectors.toList());
        prgList.addAll(newPrgList);
        prgList.forEach(prg->repo.logPrgStateExec(prg));
        repo.setPrgList(prgList);
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(p -> p.isNotCompleted()).collect(Collectors.toList());
    }

    public void displayState() throws ControllerException {
        if (displayFlag == "on")
            System.out.println(repo.getPrgList().toString());
        else
            throw new ControllerException("The display flag is not on");
    }

    public void setLogfilePath(String logfilePath) {
        this.repo.setLogFilePath(logfilePath);
    }

    public List<PrgState> getPrgList() {
        return repo.getPrgList();
    }

    public void setPrgList(List<PrgState> plist) {
        repo.setPrgList(plist);
    }
}
