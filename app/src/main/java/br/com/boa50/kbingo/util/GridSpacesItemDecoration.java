package br.com.boa50.kbingo.util;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class GridSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int qtdeItens;
    private int qtdeHeaders;
    private int[] headersPositions;

    public GridSpacesItemDecoration(int qtdeItens, int[] headersPositions) {
        this.space = 80;
        this.qtdeItens = qtdeItens;
        this.qtdeHeaders = headersPositions.length;
        this.headersPositions = headersPositions;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {


        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager grid = (GridLayoutManager) parent.getLayoutManager();


            int qtdeColunas = grid.getSpanCount();
            ArrayList<Integer> posicoes = new ArrayList<Integer>();

            if ((qtdeItens - qtdeHeaders) % qtdeColunas > 0) {
                for (int i = 0; i < headersPositions.length; i++) {
                    int proxPosition;
                    if (i == headersPositions.length - 1) {
                        proxPosition = qtdeItens;
                    } else {
                        proxPosition = headersPositions[i+1];
                    }

                    int qtdeAgrupados = proxPosition - headersPositions[i] - 1;
                    int qtdeSobrou = qtdeAgrupados % qtdeColunas;

                    posicoes.add(proxPosition - qtdeSobrou);
                    posicoes.add(proxPosition - 1);
                }

            }

            int viewPosition = parent.getChildLayoutPosition(view);

            for (int i = 0; i < posicoes.size(); i = i+2) {
                if (viewPosition >= posicoes.get(i) && viewPosition <= posicoes.get(i+1)) {
                    outRect.right = -space;
                    outRect.left = space;
                }
            }
        }
    }
}
