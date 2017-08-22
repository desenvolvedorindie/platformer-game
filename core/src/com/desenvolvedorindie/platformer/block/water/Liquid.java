package com.desenvolvedorindie.platformer.block.water;

import static com.desenvolvedorindie.platformer.block.water.Cell.CellType;
import static com.desenvolvedorindie.platformer.block.water.Cell.FlowDirection;

public class Liquid {

    // Max and min cell liquid values
    private float MaxValue = 1.0f;
    private float MinValue = 0.005f;

    // Extra liquid b cell can store than the cell above it
    private float MaxCompression = 0.25f;

    // Lowest and highest amount of liquids allowed to flow per iteration
    private float MinFlow = 0.005f;
    private float MaxFlow = 4f;

    // Adjusts flow speed (0.0f - 1.0f)
    private float FlowSpeed = 1f;

    // Keep track of modifications to cell liquid values
    private float[][] diffs;

    public void initialize(Cell[][] cells) {
        diffs = new float[cells.length][cells[0].length];
    }

    // Calculate how much liquid should flow to destination with pressure
    float calculateVerticalFlowValue(float remainingLiquid, Cell destination) {
        float sum = remainingLiquid + destination.liquid;
        float value = 0;

        if (sum <= MaxValue) {
            value = MaxValue;
        } else if (sum < 2 * MaxValue + MaxCompression) {
            value = (MaxValue * MaxValue + sum * MaxCompression) / (MaxValue + MaxCompression);
        } else {
            value = (sum + MaxCompression) / 2f;
        }

        return value;
    }

    // Run one simulation step
    public void simulate(Cell[][] cells, int w, int h) {
        float flow = 0;

        // Reset the diffs array
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                diffs[x][y] = 0;
            }
        }

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                // Get reference to Cell and reset flow
                final Cell cell = cells[x][y];
                cell.resetFlowDirections();

                // Validate cell
                if (cell.type == CellType.SOLID) {
                    cell.liquid = 0;
                    continue;
                }
                if (cell.liquid == 0)
                    continue;
                if (cell.isSettled())
                    continue;

                if (cell.liquid < MinValue) {
                    cell.liquid = 0;
                    continue;
                }

                // Keep track of how much liquid this cell started off with
                float startValue = cell.liquid;
                float remainingValue = cell.liquid;
                flow = 0;

                // Flow to bottom cell
                if (cell.bottom != null && cell.bottom.type == CellType.BLANK) {
                    // Determine rate of flow
                    flow = calculateVerticalFlowValue(cell.liquid, cell.bottom) - cell.bottom.liquid;
                    if (cell.bottom.liquid > 0 && flow > MinFlow)
                        flow *= FlowSpeed;

                    // Constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, cell.liquid))
                        flow = Math.min(MaxFlow, cell.liquid);

                    // Update temp values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x][y - 1] += flow;
                        cell.flowDirections[(int) FlowDirection.BOTTOM.id] = true;
                        cell.bottom.setSettled(false);
                    }
                }
                // Check to ensure we still have liquid in this cell
                if (remainingValue < MinValue) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Flow to left cell
                if (cell.left != null && cell.left.type == CellType.BLANK) {

                    // Calculate flow rate
                    flow = (remainingValue - cell.left.liquid) / 4f;
                    if (flow > MinFlow)
                        flow *= FlowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, remainingValue))
                        flow = Math.min(MaxFlow, remainingValue);

                    // Adjust temp values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x - 1][y] += flow;
                        cell.flowDirections[(int) FlowDirection.LEFT.id] = true;
                        cell.left.setSettled(false);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if (remainingValue < MinValue) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Flow to right cell
                if (cell.right != null && cell.right.type == CellType.BLANK) {
                    // calc flow rate
                    flow = (remainingValue - cell.right.liquid) / 3f;
                    if (flow > MinFlow)
                        flow *= FlowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, remainingValue))
                        flow = Math.min(MaxFlow, remainingValue);

                    // Adjust temp values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x + 1][y] += flow;
                        cell.flowDirections[(int) FlowDirection.RIGHT.id] = true;
                        cell.right.setSettled(false);
                    }

                }

                // Check to ensure we still have liquid in this cell
                if (remainingValue < MinValue) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Flow to Top cell
                if (cell.top != null && cell.top.type == CellType.BLANK) {
                    flow = remainingValue - calculateVerticalFlowValue(remainingValue, cell.top);
                    if (flow > MinFlow)
                        flow *= FlowSpeed;

                    // constrain flow
                    flow = Math.max(flow, 0);
                    if (flow > Math.min(MaxFlow, remainingValue))
                        flow = Math.min(MaxFlow, remainingValue);

                    // Adjust values
                    if (flow != 0) {
                        remainingValue -= flow;
                        diffs[x][y] -= flow;
                        diffs[x][y + 1] += flow;
                        cell.flowDirections[(int) FlowDirection.TOP.id] = true;
                        cell.top.setSettled(false);
                    }
                }

                // Check to ensure we still have liquid in this cell
                if (remainingValue < MinValue) {
                    diffs[x][y] -= remainingValue;
                    continue;
                }

                // Check if cell is settled
                if (startValue == remainingValue) {
                    cell.settleCount++;
                    if (cell.settleCount >= 10) {
                        cell.resetFlowDirections();
                        cell.setSettled(true);
                    }
                } else {
                    cell.unsettleNeighbors();
                }
            }
        }

        // Update Cell values
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                cells[x][y].liquid += diffs[x][y];
                if (cells[x][y].liquid < MinValue) {
                    cells[x][y].liquid = 0;
                    cells[x][y].setSettled(false); // default empty cell to
                    // unsettled
                }
            }
        }

    }

}
