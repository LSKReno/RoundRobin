import java.math.BigDecimal;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Iterator;

public class RoundRobin {

    public static void main(String[] args) {
        int quantum = 1 ;
        int clock = 0;
        int shorterTime = 0;
        int difference = 0;
        int totalTime = 0;
        int turnAround = 0;
        int waitingTime = 0;
        int originalService = 0;

        Queue<Process> readyQ = new LinkedList<>();
        Queue<Process> waitQ = new LinkedList<>();
        Iterator waitCheck = waitQ.iterator();
        Iterator emptyCheckReadyQ = readyQ.iterator();
        Iterator emptyCheckWaitQ = waitQ.iterator();

        Queue<Process>orderList = new LinkedList<>();
        LinkedList<Double> normalizedQ = new LinkedList<>();


        Scanner in = new Scanner(System.in);
        System.out.print("Enter the number of process: ");
        int size = in.nextInt();

        int[] processId = new int[size];
        int[] arriveTime = new int[size];
        int[] serviceTime = new int[size];
        // prepare the processId, arriveTime, serviceTime
        for (int i=0;i<size;i++){
            System.out.print("Enter processId of P"+(i+1)+":");
            processId[i] = in.nextInt();
        }
        for (int i=0;i<size;i++){
            System.out.print("Enter arrive time of P"+(i+1)+":");
            arriveTime[i] = in.nextInt();
        }
        for (int i=0;i<size;i++){
            System.out.print("Enter service time of P"+(i+1)+":");
            serviceTime[i] = in.nextInt();
        }

        for (int i=0;i<size;i++){
            int pId = processId[i];
            int arrive = arriveTime[i];
            int service = serviceTime[i];
            if(arrive == 0) {
                readyQ.add(new Process(pId,arrive,service,0));
            }
            else {
                waitQ.add(new Process(pId,arrive,service,0));
            }
        }

        System.out.println("--------------------------------------");
        System.out.print("Enter the quantum: ");
        int originalQuantum = in.nextInt();
        System.out.println("--------------------------------------");

        while(emptyCheckReadyQ.hasNext() || emptyCheckWaitQ.hasNext() ) {
            quantum = originalQuantum;
            Process runningProcess;

            if (emptyCheckReadyQ.hasNext()) {
                runningProcess = readyQ.remove();
            }
            else {
                runningProcess = waitQ.remove();
                clock = clock + runningProcess.getArriveTime();
            }
            // serviceTime < quantum
            if( runningProcess.getServiceTime() < quantum) {
                difference = quantum - runningProcess.getServiceTime();
                shorterTime = quantum - difference;
                quantum = shorterTime;
            }

            originalService = runningProcess.getServiceTime();
            runningProcess.setServiceTime(runningProcess.getServiceTime() - quantum);

            // serviceTime <= 0
            if(runningProcess.getServiceTime() <= 0) {
                clock = clock + quantum;
                orderList.add(runningProcess);
                runningProcess.setCs(clock);

                System.out.println("P"+ runningProcess.getId()+":" );
                System.out.println("Finish time :" + runningProcess.getCs());
                turnAround = runningProcess.getCs() - runningProcess.getArriveTime();
                System.out.println("Turnaround Time is: " + turnAround);
                System.out.println("Arrival Time : " + runningProcess.getArriveTime());
                waitingTime = turnAround - originalService;
                System.out.println("Waiting Time : " + waitingTime);

                double normalized = new BigDecimal((float)(turnAround)/(float)(serviceTime[runningProcess.getId()-1])).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                System.out.println("Normalized Turnaround Time: "+ normalized);
                normalizedQ.add(normalized);
                System.out.println();
            }
            else if(originalQuantum==1&&runningProcess.getServiceTime() > 0){
                clock = clock + quantum; 
                orderList.add(runningProcess);
                if (waitCheck.hasNext() ) {
                    Process findAt = waitQ.peek();
                    assert findAt != null;
                    if  ( findAt.getArriveTime() <= clock ) {
                        readyQ.add(waitQ.remove());
                    }
                }
                readyQ.add(runningProcess);
            }
            else if(originalQuantum!=1 && runningProcess.getServiceTime() > 0){
                clock = clock + quantum;
                orderList.add(runningProcess);
                if (waitCheck.hasNext() ) {
                    Process findAt = waitQ.peek();
                    assert findAt != null;
                    if  ( findAt.getArriveTime() <= clock ) {
                        readyQ.add(waitQ.remove());
                    }
                }
                waitQ.add(runningProcess);
            }

        }

        //check for queue
        System.out.println("--------------------------------------");
        Iterator orderPrint = orderList.iterator();
        System.out.println("Order of Processes:");
        while(orderPrint.hasNext()) {
            Process firstElement = orderList.remove();
            System.out.print(" P" + firstElement.getId());
        }

        totalTime = clock;
        System.out.println("\n--------------------------------------");
        System.out.println("Total runtime: " + totalTime);
        System.out.println("--------------------------------------");

        Double sum =0.;
        for (int i=0;i<normalizedQ.size();i++){
            sum += normalizedQ.get(i);
        }
        double averageNormalizedTime = sum/normalizedQ.size();
        System.out.println("Average normalized time: "+averageNormalizedTime);

    }

    static class Process {
        int id;
        int arriveTime;
        int serviceTime;
        int cs;

        public Process(int id, int arriveTime, int serviceTime, int cs) {
            this.id = id;
            this.arriveTime = arriveTime;
            this.serviceTime = serviceTime;
            this.cs = cs;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getArriveTime() {
            return arriveTime;
        }

        public void setArriveTime(int arriveTime) {
            this.arriveTime = arriveTime;
        }

        public int getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(int serviceTime) {
            this.serviceTime = serviceTime;
        }

        public int getCs() {
            return cs;
        }

        public void setCs(int cs) {
            this.cs = cs;
        }
    }

}



























