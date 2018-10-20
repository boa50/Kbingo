package br.com.boa50.kbingo.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Pedra;

public final class CartelaUtils {
    private static final int linhas = 6;
    private static final int colunas = 5;
    private static final int espacamento = 38;
    private static final int largura = espacamento * colunas;
    private static final int altura = espacamento * linhas;
    private static final int larguraLinha = 2;
    private static final int radiusCirculoCentral = espacamento/5;
    private static final int textPedraSize = espacamento/2 + espacamento/8;
    private static final int textCartelaSize = espacamento/4;
    private static final int espacamentoPadrao = (espacamento - larguraLinha)/2;
    private static final int alturaCartelaBitmap = altura + textCartelaSize*2;

    public static boolean hasCartelaPedra(List<CartelaPedra> cartelaPedras, Pedra pedra) {
        for (CartelaPedra cartelaPedra : cartelaPedras) {
            if (cartelaPedra.getPedraId() == pedra.getId()) return true;
        }
        return false;
    }

    public static boolean validarExportarCartelas(int idInicial, int idFinal) {
        return idInicial <= idFinal;
    }

    public static File gerarCartelas(List<String> letras, List<CartelaPedra> cartelasPedras, File directory) {
        int idInicial = cartelasPedras.get(0).getCartelaId();
        int idFinal = cartelasPedras.get(cartelasPedras.size() - 1).getCartelaId();

        Document document = new Document();
        File file = new File(directory,"cartelas.pdf");

        try {
            if (!directory.exists()) directory.mkdirs();
            if (file.exists()) file.delete();
            file.createNewFile();

            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Kbingo");
            document.addCreator("Kbingo");

            Bitmap cartela = Bitmap.createBitmap(largura*2 + 120,
                    alturaCartelaBitmap*3 + 10, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(cartela);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            int ultimaPosicao = 0;
            for (int i = 1; i <= (idFinal - idInicial + 1); i++) {
                if (ultimaPosicao > 5) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    cartela.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    document.add(Image.getInstance(stream.toByteArray()));

                    cartela = Bitmap.createBitmap(largura*2 + 120,
                            alturaCartelaBitmap*3 + 10, Bitmap.Config.ARGB_8888);
                    canvas = new Canvas(cartela);
                    paint = new Paint();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(Color.WHITE);
                    canvas.drawPaint(paint);

                    ultimaPosicao = 0;
                }
                canvas = gerarCartela(canvas,
                        ultimaPosicao,
                        letras,
                        cartelasPedras.subList(24*(i - 1), 24*i));
                ultimaPosicao ++;
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            cartela.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            document.add(Image.getInstance(stream.toByteArray()));

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private static Canvas gerarCartela(Canvas canvas, int posicao, List<String> letras,
                                List<CartelaPedra> cartelaPedras) {
        int deslocamentoX = 20;
        int deslocamentoY = 0;
        switch (posicao) {
            case 1:
                deslocamentoX += largura + 100;
                deslocamentoY = 0;
                break;
            case 2:
                deslocamentoY = alturaCartelaBitmap + 3;
                break;
            case 3:
                deslocamentoX += largura + 100;
                deslocamentoY = alturaCartelaBitmap + 3;
                break;
            case 4:
                deslocamentoY = alturaCartelaBitmap*2 + 6;
                break;
            case 5:
                deslocamentoX += largura + 100;
                deslocamentoY = alturaCartelaBitmap*2 + 6;
                break;
        }

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(larguraLinha);

        int paddingLinha;
        int inicioColuna;
        for (int i = 0; i <= linhas; i++) {
            if (i == 0) {
                paddingLinha = larguraLinha/2;
            } else if (i == linhas) {
                paddingLinha = -larguraLinha/2;
            } else {
                paddingLinha = 0;
            }

            canvas.drawLine(deslocamentoX,
                    deslocamentoY + (altura/linhas)*i + paddingLinha,
                    deslocamentoX + largura,
                    deslocamentoY + (altura/linhas)*i + paddingLinha,
                    paint);

            if (i <= colunas) {
                if (i == 0 || i == colunas) {
                    inicioColuna = 0;
                } else {
                    inicioColuna = altura/linhas;
                }

                if (i == colunas) paddingLinha = -larguraLinha/2;

                canvas.drawLine(deslocamentoX + (largura/colunas)*i + paddingLinha,
                        deslocamentoY + inicioColuna,
                        deslocamentoX + (largura/colunas)*i + paddingLinha,
                        deslocamentoY + altura,
                        paint);
            }
        }

        paint.setAntiAlias(true);
        paint.setTextSize(textPedraSize);
        int espacamentoX;
        int espacamentoY;

        Collections.sort(cartelaPedras, (cartelaPedra1, cartelaPedra2) ->
                Integer.compare(Integer.compare(cartelaPedra1.getColuna(), cartelaPedra2.getColuna()),
                        Integer.compare(cartelaPedra2.getLinha(), cartelaPedra1.getLinha())));

        int contadorPedras = 0;
        for (int i = 0; i < 5; i++) {
            espacamentoX = espacamentoPadrao;
            espacamentoY = espacamentoPadrao;

            Rect r = new Rect();
            paint.getTextBounds(StringUtils.formatarNumeroPedra(
                    cartelaPedras.get(contadorPedras).getPedraId()),
                    0, 2, r);
            espacamentoX -= Math.ceil(r.width()/2.);
            espacamentoY += Math.abs(r.height()/2.);

            for (int j = 0; j < 5; j++) {
                if (i == 2 && j == 2) {
                    canvas.drawCircle(deslocamentoX + (largura/colunas)*i + espacamento/2,
                            deslocamentoY + (altura/linhas)*(j + 1) + espacamento/2,
                            radiusCirculoCentral,
                            paint);
                } else {
                    canvas.drawText(StringUtils.formatarNumeroPedra(
                            cartelaPedras.get(contadorPedras).getPedraId()),
                            deslocamentoX + (largura/colunas)*i + espacamentoX,
                            deslocamentoY + (altura/linhas)*(j + 1) + espacamentoY,
                            paint);
                    contadorPedras++;
                }
            }
        }

        paint.setColor(Color.RED);
        for (int i = 0; i < 5; i++) {
            espacamentoX = espacamentoPadrao;
            espacamentoY = espacamentoPadrao;

            Rect r = new Rect();
            paint.getTextBounds(letras.get(i), 0, letras.get(i).length(), r);
            espacamentoX -= Math.ceil(r.width()/2.);
            espacamentoY += Math.abs(r.height()/2.);

            canvas.drawText(letras.get(i),
                    deslocamentoX + (largura/colunas)*i + espacamentoX,
                    deslocamentoY + espacamentoY,
                    paint);
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(textCartelaSize);
        String textoCartela = "Cartela: " + StringUtils.formatarNumeroCartela(
                cartelaPedras.get(0).getCartelaId());
        Rect r = new Rect();
        paint.getTextBounds(textoCartela, 0, textoCartela.length(), r);
        canvas.drawText(textoCartela,
                deslocamentoX + largura - r.width() - textCartelaSize/2,
                deslocamentoY + altura + r.height() + larguraLinha,
                paint);

        return canvas;
    }
}
