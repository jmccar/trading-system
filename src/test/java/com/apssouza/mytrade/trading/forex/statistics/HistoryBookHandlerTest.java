package com.apssouza.mytrade.trading.forex.statistics;

import com.apssouza.mytrade.trading.builder.FilledOrderBuilder;
import com.apssouza.mytrade.trading.builder.OrderBuilder;
import com.apssouza.mytrade.trading.builder.PositionBuilder;
import com.apssouza.mytrade.trading.forex.order.OrderDto;
import com.apssouza.mytrade.trading.forex.portfolio.FilledOrderDto;
import com.apssouza.mytrade.trading.forex.portfolio.Position;
import com.apssouza.mytrade.trading.forex.session.CycleHistory;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HistoryBookHandlerTest extends TestCase {

    private HistoryBookHandler historyBookHandler;

    @Mock
    TransactionsExporter transactionsExporter;

    @Before
    public  void setUp(){
//        Mockito.when(transactionsExporter.exportCsv(Mockito.anyList(), Mockito.anyString()))
        this.historyBookHandler = new HistoryBookHandler(transactionsExporter);
    }

    @Test
    public void getTransactions() {
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void startCycle() {
        historyBookHandler.startCycle(LocalDateTime.MIN);
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        CycleHistory cycleHistory = transactions.get(0);
        assertEquals(LocalDateTime.MIN, cycleHistory.getTime());
    }

    @Test
    public void endCycle() {
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        assertEquals(1, transactions.size());
    }

    @Test
    public void setState() {
        historyBookHandler.startCycle(LocalDateTime.MIN);
        historyBookHandler.setState(TransactionState.EXIT,"AUDUSD");
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        CycleHistory cycleHistory = transactions.get(0);
        assertEquals(TransactionState.EXIT, cycleHistory.getTransactions().get("AUDUSD").getState());
    }

    @Test
    public void addPosition() {
        historyBookHandler.startCycle(LocalDateTime.MIN);
        PositionBuilder positionBuilder = new PositionBuilder();
        Position position = positionBuilder.build();
        historyBookHandler.addPosition(position);
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        CycleHistory cycleHistory = transactions.get(0);
        assertEquals(position, cycleHistory.getTransactions().get("AUDUSD").getPosition());
    }

    @Test
    public void addOrderFilled() {
        historyBookHandler.startCycle(LocalDateTime.MIN);
        FilledOrderDto orderDto =  new FilledOrderBuilder().build();
        historyBookHandler.addOrderFilled(orderDto);
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        CycleHistory cycleHistory = transactions.get(0);
        assertEquals(orderDto, cycleHistory.getTransactions().get("AUDUSD").getFilledOrder());
    }

    @Test
    public void addOrder() {
        historyBookHandler.startCycle(LocalDateTime.MIN);
        OrderDto orderDto =  new OrderBuilder().build();
        historyBookHandler.addOrder(orderDto);
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        CycleHistory cycleHistory = transactions.get(0);
        assertEquals(orderDto, cycleHistory.getTransactions().get("AUDUSD").getOrder());
    }


    @Test
    public void addOrderToDifferentCpair() {
        historyBookHandler.startCycle(LocalDateTime.MIN);
        OrderBuilder orderBuilder = new OrderBuilder();
        OrderDto orderDto =  orderBuilder.build();
        OrderDto orderDto2 =  orderBuilder.setIdentifier("EURUSD").setSymbol("EURUSD").build();
        historyBookHandler.addOrder(orderDto);
        historyBookHandler.addOrder(orderDto2);
        historyBookHandler.endCycle();
        List<CycleHistory> transactions = historyBookHandler.getTransactions();
        CycleHistory cycleHistory = transactions.get(0);
        assertEquals(orderDto, cycleHistory.getTransactions().get("AUDUSD").getOrder());
        assertEquals(orderDto2, cycleHistory.getTransactions().get("EURUSD").getOrder());
    }

    @Test
    public void export() {
        historyBookHandler.export("");
    }
}