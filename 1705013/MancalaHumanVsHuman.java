import java.util.ArrayList;
import java.util.Scanner;

public class MancalaHumanVsHuman {
    public static void main(String[] args) {
        int[] player1=new int[10];
        int[] player2=new int[10];
        for(int i=0;i<7;i++){
            if(i==0){
                player1[i]=0;
                player2[i]=0;
            }
            else{
                player1[i]=4;
                player2[i]=4;
            }
        }
        printBoard(player1, player2);
        Board board=new Board(player1,player2,false);
        int turn=1;
        while (true){
            if(EndGame(board)){
                System.out.println("Game Over!!!");
                System.out.println("Player1: "+board.getPlayer1()[0]);
                System.out.println("Player2: "+board.getPlayer2()[0]);
                if(board.getPlayer2()[0]>board.getPlayer1()[0])
                    System.out.println("Player2 Wins");
                else if(board.getPlayer2()[0]<board.getPlayer1()[0])
                    System.out.println("Player1 Wins");
                else{
                    System.out.println("Match Drawn");
                }
                break;
            }
            else{
                System.out.println("Turn of Player"+turn+": ");
                Scanner scanner = new Scanner(System.in);
                int position=scanner.nextInt();
                turn=doMove(board,turn,position);
            }
        }
    }
    public static void printBoard(int[] player1, int[] player2){
        System.out.println("Current Board position----------");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println("|         |   "+player1[6]+"   |   "+player1[5]+"   |   "+player1[4]+"   |   "+player1[3]+"   |   "+player1[2]+"   |   "+player1[1]+"   |         |");
        System.out.println("|    "+player1[0]+"    |=======|=======|=======|=======|=======|=======|    "+player2[0]+"    |");
        System.out.println("|         |   "+player2[1]+"   |   "+player2[2]+"   |   "+player2[3]+"   |   "+player2[4]+"   |   "+player2[5]+"   |   "+player2[6]+"   |         |");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
    }
    public static int doMove(Board board,int turn,int position){
        int NumOfballs;
        String flag="Turn";
        if(turn==1){
            int currentBoard=1;
            NumOfballs=board.getPlayer1()[position];
            board.getPlayer1()[position]=0;
            for(int i=1;i<=NumOfballs;i++){
                position++;
                if(position<=6 && currentBoard==1){
                    if(board.getPlayer1()[position]==0 && (i==NumOfballs) && board.getPlayer2()[7-position]!=0){
                        System.out.println("Good Capture!!");
                        board.getPlayer1()[0]+=1+board.getPlayer2()[7-position];
                        board.getPlayer2()[7-position]=0;
                    }
                    else{
                        board.getPlayer1()[position]+=1;
                    }
                }
                else if(position==7 && currentBoard==1){
                    board.getPlayer1()[0]+=1;
                    currentBoard=2;
                    position=0;
                    if(i==NumOfballs){
                        System.out.println("Free Move of Player"+turn);
                        flag="Free move";
                    }
                }
                else if(position<=6 && currentBoard==2){
                    board.getPlayer2()[position]+=1;
                }
                else if(position==7 && currentBoard==2){
                    position=0;
                    currentBoard=1;
                    i--;
                }
            }
        }
        else{
            int currentBoard=2;
            NumOfballs=board.getPlayer2()[position];
            board.getPlayer2()[position]=0;
            for(int i=1;i<=NumOfballs;i++){
                position++;
                if(position<=6 && currentBoard==2){
                    if(board.getPlayer2()[position]==0 && (i==NumOfballs) && board.getPlayer1()[7-position]!=0){
                        System.out.println("Good Capture!!");
                        board.getPlayer2()[0]+=1+board.getPlayer1()[7-position];
                        board.getPlayer1()[7-position]=0;
                    }
                    else{
                        board.getPlayer2()[position]+=1;
                    }
                }
                else if(position==7 && currentBoard==2){
                    board.getPlayer2()[0]+=1;
                    currentBoard=1;
                    position=0;
                    if(i==NumOfballs){
                        System.out.println("Free Move of Player"+turn);
                        flag="Free move";
                    }
                }
                else if(position<=6 && currentBoard==1){
                    board.getPlayer1()[position]+=1;
                }
                else if(position==7 && currentBoard==1){
                    position=0;
                    currentBoard=2;
                    i--;
                }
            }
        }
        printBoard(board.getPlayer1(),board.getPlayer2());
        if(flag.equals("Free move") && turn==1)
            return turn;
        else if(flag.equals("Free move") && turn==2)
            return turn;
        else if(flag.equals("Turn") && turn==1)
            return 2;
        else
            return 1;
    }
    public static boolean EndGame(Board board){
        boolean isEnd=false;
        int isZero=0;
        for(int i=1;i<7;i++){
            if(board.getPlayer1()[i]==0){
                isZero++;
            }
        }
        if(isZero==6){
            isEnd=true;
            for(int i=1;i<7;i++){
                board.getPlayer2()[0]+=board.getPlayer2()[i];
            }
            return isEnd;
        }
        isZero=0;
        for(int i=1;i<7;i++){
            if(board.getPlayer2()[i]==0){
                isZero++;
            }
        }
        if(isZero==6){
            isEnd=true;
            for(int i=1;i<7;i++){
                board.getPlayer1()[0]+=board.getPlayer1()[i];
            }
            return isEnd;
        }
        return isEnd;
    }
}
