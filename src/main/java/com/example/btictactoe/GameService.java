package com.example.btictactoe;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    private final Random rnd = new Random();

    public PlayResponse startGame() {
        int[] board = new int[9];
        return new PlayResponse(board, GameStatus.IN_PROGRESS, 0);
    }

    public PlayResponse play(int[] board, int move) {
         
        if (board == null || board.length != 9) {
            throw new IllegalArgumentException("Board must be length 9");
        }
        if (move < 0 || move > 8) {
            throw new IllegalArgumentException("Move index must be 0..8");
        }

        
        int currentWinner = checkWinner(board);
        if (currentWinner != 0) {
            GameStatus status = currentWinner == 1 ? GameStatus.PLAYER_WON :
                                currentWinner == 2 ? GameStatus.AI_WON : GameStatus.DRAW;
            return new PlayResponse(board, status, currentWinner);
        }

        
        if (board[move] != 0) {
            
            return new PlayResponse(board, GameStatus.INVALID_MOVE, 0);
        }

        
        board[move] = 1;

        
        currentWinner = checkWinner(board);
        if (currentWinner == 1) {
            return new PlayResponse(board, GameStatus.PLAYER_WON, 1);
        }
        if (isBoardFull(board)) {
            return new PlayResponse(board, GameStatus.DRAW, 0);
        }

      
        int aiMove = computeAIMove(board);
        if (aiMove != -1) {
            board[aiMove] = 2;
        }

        
        currentWinner = checkWinner(board);
        if (currentWinner == 2) {
            return new PlayResponse(board, GameStatus.AI_WON, 2);
        }
        if (isBoardFull(board)) {
            return new PlayResponse(board, GameStatus.DRAW, 0);
        }

        
        return new PlayResponse(board, GameStatus.IN_PROGRESS, 0);
    }

   
    private boolean isBoardFull(int[] board) {
        for (int v : board) {
            if (v == 0) return false;
        }
        return true;
    }

    
    private int checkWinner(int[] b) {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8}, 
                {0,3,6}, {1,4,7}, {2,5,8}, 
                {0,4,8}, {2,4,6}           
        };
        for (int[] w : wins) {
            if (b[w[0]] != 0 && b[w[0]] == b[w[1]] && b[w[1]] == b[w[2]]) {
                return b[w[0]];
            }
        }
        return 0;
    }

    
    private int computeAIMove(int[] board) {
        
        int winMove = findWinningMove(board, 2);
        if (winMove != -1) return winMove;

        
        int blockMove = findWinningMove(board, 1);
        if (blockMove != -1) return blockMove;

      
        if (board[4] == 0) return 4;

        
        int[] corners = {0,2,6,8};
        List<Integer> freeCorners = new ArrayList<>();
        for (int c : corners) if (board[c] == 0) freeCorners.add(c);
        if (!freeCorners.isEmpty()) return freeCorners.get(rnd.nextInt(freeCorners.size()));

       
        int[] sides = {1,3,5,7};
        List<Integer> freeSides = new ArrayList<>();
        for (int s : sides) if (board[s] == 0) freeSides.add(s);
        if (!freeSides.isEmpty()) return freeSides.get(rnd.nextInt(freeSides.size()));

        
        return -1;
    }

    
    private int findWinningMove(int[] board, int player) {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };
        for (int[] w : wins) {
            int a = w[0], b = w[1], c = w[2];
         
            if ((board[a] == player && board[b] == player && board[c] == 0)) return c;
            if ((board[a] == player && board[c] == player && board[b] == 0)) return b;
            if ((board[b] == player && board[c] == player && board[a] == 0)) return a;
        }
        return -1;
    }
}
