package com.apssouza.mytrade.trading.forex.statistics;

import com.apssouza.mytrade.trading.forex.session.CycleHistory;
import com.apssouza.mytrade.trading.forex.session.TransactionDto;
import com.apssouza.mytrade.trading.misc.helper.file.WriteFileHelper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TransactionsExporter {

    public void exportCsv(List<CycleHistory> transactions, String filepath) {
        WriteFileHelper.write(filepath,"");
        for (CycleHistory item : transactions) {
            for (Map.Entry<String, TransactionDto> trans : item.getTransactions().entrySet()) {
                List<String> line = Arrays.asList(
                        trans.getValue().getIdentifier(),
                        trans.getValue().getPosition() != null ? toString(trans.getValue().getPosition().getInitPrice()) : "",
                        trans.getValue().getPosition() != null ? toString(trans.getValue().getPosition().getQuantity()) : "",
                        trans.getValue().getPosition() != null ? toString(trans.getValue().getPosition().getInitPrice().multiply(BigDecimal.valueOf(trans.getValue().getPosition().getQuantity()))) : "",
                        trans.getValue().getOrder() != null ? toString(trans.getValue().getOrder().getAction()) : "",
                        trans.getValue().getPosition() != null ? toString(trans.getValue().getPosition().getTimestamp()) : "",
                        //trans.getValue().getPosition() != null ? trans.getValue().getPosition().getPlacedStopLoss().getPrice().toString(): "",
                        //trans.getValue().getPosition() != null ? trans.getValue().getPosition().getTakeProfitOrder().getPrice().toString(): "",
                        trans.getValue().getPosition() != null ? toString(trans.getValue().getPosition().getExitReason()) : "",
                        trans.getValue().getState() != null ? toString(trans.getValue().getState()) : "",
                        ""
                );
                WriteFileHelper.append(filepath, String.join(",", line) + "\n");
            }
        }
    }

    private String toString(Object ob){
        if (ob != null){
            return ob.toString();
        }
        return "";
    }
}