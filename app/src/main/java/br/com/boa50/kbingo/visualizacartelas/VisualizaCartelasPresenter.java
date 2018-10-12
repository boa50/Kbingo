package br.com.boa50.kbingo.visualizacartelas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.annotation.NonNull;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.data.entity.Letra;
import br.com.boa50.kbingo.util.CartelaUtils;
import br.com.boa50.kbingo.util.StringUtils;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VisualizaCartelasPresenter implements VisualizaCartelasContract.Presenter {

    private VisualizaCartelasContract.View mView;

    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private CompositeDisposable mCompositeDisposable;

    private final int linhas = 6;
    private final int colunas = 5;
    private final int espacamento = 38;
    private final int largura = espacamento * colunas;
    private final int altura = espacamento * linhas;
    private final int larguraLinha = 2;
    private final int radiusCirculoCentral = espacamento/5;
    private final int textPedraSize = espacamento/2 + espacamento/8;
    private final int textCartelaSize = espacamento/4;
    private final int espacamentoPadrao = (espacamento - larguraLinha)/2;
    private final int alturaCartelaBitmap = altura + textCartelaSize*2;

    @Inject
    VisualizaCartelasPresenter(@NonNull AppDataSource appDataSource,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        mAppDataSource = appDataSource;
        mScheduleProvider = schedulerProvider;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe(@NonNull VisualizaCartelasContract.View view) {
        mView = view;
        iniciarLayout();
    }

    @Override
    public void unsubscribe() {
        mView = null;
        mCompositeDisposable.clear();
    }

    private void iniciarLayout() {
        Disposable disposable = mAppDataSource
                .getLetras()
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        letras -> mView.iniciarLayout(letras)
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void carregarCartela(int id, boolean confereCartela) {
        Disposable disposable = mAppDataSource
                .getPedrasByCartelaId(id)
                .subscribeOn(mScheduleProvider.io())
                .observeOn(mScheduleProvider.ui())
                .subscribe(
                        cartelaPedras -> {
                            if (cartelaPedras.size() > 0) {
                                if (confereCartela) {
                                    Disposable disposable2 = mAppDataSource
                                            .getPedras()
                                            .subscribeOn(mScheduleProvider.io())
                                            .observeOn(mScheduleProvider.ui())
                                            .subscribe(
                                                    pedras -> mView.apresentarCartela(cartelaPedras, pedras)
                                            );

                                    mCompositeDisposable.add(disposable2);
                                } else {
                                    mView.apresentarCartela(cartelaPedras, null);
                                }
                            } else {
                                Disposable disposable2 = mAppDataSource
                                        .getCartelaUltimoId()
                                        .subscribeOn(mScheduleProvider.io())
                                        .observeOn(mScheduleProvider.ui())
                                        .subscribe(
                                                cartelaId ->{
                                                    carregarCartela(cartelaId, confereCartela);
                                                    mView.apresentarMaximoIdCartela(cartelaId);
                                                }
                                        );

                                mCompositeDisposable.add(disposable2);
                            }
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void prepararDialogExportar(int idInicial, int idFinal) {
        if (idInicial > 0 && idFinal > 0) {
            mView.abrirDialogExportarCartelas(idInicial, idFinal);
        } else {
            mCompositeDisposable.add(mAppDataSource
                    .getCartelaUltimoId()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            cartelaId -> mView.abrirDialogExportarCartelas(1, cartelaId)
                    ));
        }
    }

    @Override
    public void exportarCartelas(int idInicial, int idFinal) {
        if (idInicial > idFinal) {
            mView.mostrarMensagensIdsIncompativeis();
        }

        Document document = new Document();
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
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

            List<Integer> cartelasIds = new ArrayList<>();
            for (int i = idInicial; i <= idFinal; i++) {
                cartelasIds.add(i);
            }

            mCompositeDisposable.add(mAppDataSource
                    .getLetras()
                    .subscribeOn(mScheduleProvider.io())
                    .observeOn(mScheduleProvider.ui())
                    .subscribe(
                            letras -> {
                                List<String> letrasLetra = new ArrayList<>();
                                for (Letra letra : letras) {
                                    letrasLetra.add(letra.getNome());
                                }

                                mCompositeDisposable.add(mAppDataSource
                                        .getPedrasByCartelasIds(cartelasIds)
                                        .subscribeOn(mScheduleProvider.io())
                                        .observeOn(mScheduleProvider.ui())
                                        .subscribe(
                                                cartelaPedras -> {
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
                                                                letrasLetra,
                                                                cartelaPedras.subList(24*(i - 1), 24*i));
                                                        ultimaPosicao ++;
                                                    }

                                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                                    cartela.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                                    document.add(Image.getInstance(stream.toByteArray()));

                                                    document.close();

                                                    mView.realizarDownload(file);
                                                }
                                        ));
                            }
                    ));

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Canvas gerarCartela(Canvas canvas, int posicao, List<String> letras,
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
        String textoCartela = "Cartela: " + CartelaUtils.formatarNumeroCartela(
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
