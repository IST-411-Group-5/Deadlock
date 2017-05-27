package deadlock;

/**
 *
 * @authors Eric Ruppert, Susan Tabassum, Erik Galloway, Miao Yu
 */

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Bank implementation from the text
 * Utilizes the bank account example from the text to create the condition
 */
 
// Bank Class from Core Java Volume 1 Listing 14.7
   
public class Bank {
    
    private final double[] accounts;
    private Lock bankLock;
    private Condition sufficientFunds;
    
    /** Constructor
     * 
     * @param n Number of accounts to create
     * @param initialBalance Initial balance in each account
     */
    public Bank(int n, double initialBalance) {
        accounts = new double[n];
        Arrays.fill(accounts, initialBalance);
        bankLock = new ReentrantLock();
        sufficientFunds = bankLock.newCondition();
    }
    /**
     * 
     * @param from the account to transfer from
     * @param to the account to transfer to
     * @param amount the amount to transfer
     * @throws InterruptedException 
     */
    public void transfer(int from, int to, double amount) throws InterruptedException{
        bankLock.lock();
        try{
            while(accounts[from] < amount){
                sufficientFunds.await();
            }
            
            accounts[from] -= amount;
            
            System.out.printf(Thread.currentThread() + " %10.2f from %d to %d", amount, from, to);
            accounts[to] += amount;
            
            double sum = 0;
            for (double a : accounts)
               sum += a;
            System.out.printf(" Total Balance: %10.2f%n", sum);
            sufficientFunds.signalAll();
        }
        finally{
            bankLock.unlock();
        }
    }
    
}
