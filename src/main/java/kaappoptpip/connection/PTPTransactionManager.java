package kaappoptpip.connection;

import kaappoptpip.packet.in.PTPDataPacketIn;
import kaappoptpip.packet.in.PTPPacketInStartData;
import kaappoptpip.packet.out.PTPPacketCmdRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PTPTransactionManager {
    private Map<Integer, PTPTransaction> transactions;
    private List<CompletedPTPTransaction> completeTransactions;

//    public static final Object lock = new Object();

    public PTPTransactionManager () {
        transactions = new HashMap<>();
        completeTransactions = new ArrayList<>();
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

    public List<CompletedPTPTransaction> getCompleteTransactions () {
        var toReturn = completeTransactions;
        completeTransactions = new ArrayList<>();
        return toReturn;
    }
}
