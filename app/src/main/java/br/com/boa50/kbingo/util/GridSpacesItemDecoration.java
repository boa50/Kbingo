package br.com.boa50.kbingo.util;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GridSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int qtdItens;
    private int qtdHeaders;
    private List<Integer> headersPositions;
    private List<Integer> positions;
    private List<Integer> spaces;

    public GridSpacesItemDecoration(int qtdItens, List<Integer> headersPositions) {
        this.qtdItens = qtdItens;
        this.qtdHeaders = headersPositions.size();
        this.headersPositions = headersPositions;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager grid = (GridLayoutManager) parent.getLayoutManager();

            if (positions == null)
                fillPositionsSpaces(grid.getSpanCount(), parent.getWidth());

            int viewPosition = parent.getChildLayoutPosition(view);

            for (int i = 0; i < positions.size()/2; i++) {
                if (viewPosition >= positions.get(i*2) && viewPosition <= positions.get(i*2+1)) {
                    outRect.right = -spaces.get(i);
                    outRect.left = spaces.get(i);
                }
            }
        }
    }

    private void fillPositionsSpaces(int qtdColumns, int parentWidth){
        positions = new ArrayList<>();
        spaces = new ArrayList<>();
        int columnSize = parentWidth/qtdColumns;

        if ((qtdItens - qtdHeaders) % qtdColumns > 0) {
            for (int i = 0; i < headersPositions.size(); i++) {
                int nextPosition;
                if (i == headersPositions.size() - 1) {
                    nextPosition = qtdItens;
                } else {
                    nextPosition = headersPositions.get(i+1);
                }

                int qtdGrouped = nextPosition - headersPositions.get(i) - 1;
                int qtdLast = qtdGrouped % qtdColumns;

                spaces.add((columnSize * (qtdColumns - qtdLast)) / 2);

                positions.add(nextPosition - qtdLast);
                positions.add(nextPosition - 1);
            }

        }
    }
}
