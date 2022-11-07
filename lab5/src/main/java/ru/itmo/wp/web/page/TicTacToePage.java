package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {

    private void action(HttpServletRequest request, Map<String, Object> view) {
        State state = (State) request.getSession().getAttribute("state");

        if (state == null) {
            state = new State();
            request.getSession().setAttribute("state", state);
        }

        view.put("state", state);
    }

    private String extractCell(HttpServletRequest request) {
        String cell = null;

        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String element = enumeration.nextElement();
            if (element.matches("cell_[0-9][0-9]")) {
                cell = element;
                break;
            }
        }

        return cell;
    }

    private void onMove(HttpServletRequest request) {
        String cell = extractCell(request);
        if (cell == null) {
            throw new RedirectException(request.getRequestURI());
        }

        State state = (State) request.getSession().getAttribute("state");

        if (state == null) {
            state = new State();
        }

        state.makeMove(cell.charAt(5) - '0', cell.charAt(6) - '0');
        request.getSession().setAttribute("state", state);
        throw new RedirectException(request.getRequestURI());
    }

    private void newGame(HttpServletRequest request) {
        request.getSession().setAttribute("state", null);
        throw new RedirectException(request.getRequestURI());
    }

    public static class State implements Serializable {

        private final int size = 3;
        private final Character[][] cells;

        private boolean crossesMove = true;
        private String phase = "RUNNING";

        private int freeCells;

        public State() {
            cells = new Character[size][size];
            freeCells = size * size;
        }

        public Character[][] getCells() {
            return cells;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public int getSize() {
            return size;
        }

        public String getPhase() {
            return phase;
        }

        public void makeMove(int x, int y) {
            if (!validCord(x) || !validCord(y)) {
                return;
            }

            if (phase.equals("RUNNING")) {
                cells[x][y] = crossesMove ? 'X' : 'O';
                crossesMove = !crossesMove;
                freeCells--;

                checkFinish();
            }
        }

        private void checkFinish() {
            for (int i = 0; i < size; i++) {
                // ROWS
                checkLine(0, i, 1, 0);
                // COLUMNS
                checkLine(i, 0, 0, 1);
            }
            // DIAGONALS
            checkLine(0, 0, 1, 1);
            checkLine(size - 1, 0, -1, 1);

            checkDraw();
        }

        private void checkLine(int startX, int startY, int deltaX, int deltaY) {
            char sign = crossesMove ? 'O' : 'X';

            boolean finished = true;
            for (int x = startX, y = startY; validCord(x) && validCord(y); x += deltaX, y += deltaY) {
                finished = finished && Objects.equals(cells[x][y], sign);
            }

            if (finished) {
                phase = crossesMove ? "WON_O" : "WON_X";
            }
        }

        private boolean validCord(int cord) {
            return 0 <= cord && cord < size;
        }

        private void checkDraw() {
            if (freeCells == 0 && phase.equals("RUNNING")) {
                phase = "DRAW";
            }
        }
    }
}
