package kaappoptpip.transaction;

import kaappoptpip.packet.in.PTPDataPacketIn;
import kaappoptpip.packet.in.PTPPacketIn;
import kaappoptpip.packet.in.PTPPacketInStartData;
import kaappoptpip.packet._out.PTPPacketCmdRequest;

import java.util.*;
import java.util.stream.Collectors;

public class PTPTransactionManager {
    private final Map<Integer, PTPTransaction> transactions;
    private Set<PTPCompletedDataTransfer> completeTransactions;

//    public static final Object lock = new Object();

    public PTPTransactionManager () {
        transactions = new HashMap<>();
        completeTransactions = new HashSet<>();
    }

    public void startTransaction (int transactionID, PTPPacketCmdRequest initiatingPacket) {
        transactions.put(transactionID, new PTPTransaction(transactionID, initiatingPacket));
    }

    public void addDataToTransaction (int transactionID, PTPDataPacketIn data) {
        PTPTransaction transaction = transactions.get(transactionID);
        transaction.addData(data);
        if (transaction.isComplete()) {
            completeTransactions.add(transaction.getTransactionData());
            transactions.remove(transactionID);
        }
    }

    public void addDataToTransaction (int transactionID, PTPPacketInStartData data) {
        PTPTransaction transaction = transactions.get(transactionID);
        transaction.addData(data);
    }

    public boolean haveTransactionsCompleted () {
        return completeTransactions.size() > 0;
    }

    public Set<PTPCompletedDataTransfer> getCompleteTransactions () {
        return completeTransactions;
    }

    public boolean hasCompletedTransactionFor (PTPPacketIn packet) {
        if (packet == null) return false;
        return completeTransactions.stream().map(PTPCompletedDataTransfer::getTransactionID).anyMatch(id -> packet.getTransactionID() == id);
    }

    public PTPCompletedDataTransfer getCompleteTransactionFor (PTPPacketIn packet) {
        Set<PTPCompletedDataTransfer> eligibleTransactions = completeTransactions
                .stream()
                .filter(transaction -> transaction.getTransactionID() == packet.getTransactionID())
                .collect(Collectors.toSet());

        if (eligibleTransactions.size() != 1) {
            throw new RuntimeException("Got wrong amount of completedTransactions for packet " + packet + "! Expected only one, got " + eligibleTransactions + "!");
        } else {
            PTPCompletedDataTransfer transactionResult = eligibleTransactions.stream().findFirst().orElseThrow();
            completeTransactions.remove(transactionResult);
            return transactionResult;
        }
    }

    @Override
    public String toString () {
        return "PTPTransactionManager{" +
                "completeTransactions=" + completeTransactions +
                '}';
    }
}
