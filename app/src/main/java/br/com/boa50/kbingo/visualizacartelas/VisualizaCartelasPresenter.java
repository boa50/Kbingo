package br.com.boa50.kbingo.visualizacartelas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.Lists;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
import br.com.boa50.kbingo.data.entity.CartelaPedra;
import br.com.boa50.kbingo.util.CartelaUtils;
import br.com.boa50.kbingo.util.PedraUtils;
import br.com.boa50.kbingo.util.StringUtils;
import br.com.boa50.kbingo.util.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class VisualizaCartelasPresenter implements VisualizaCartelasContract.Presenter {

    private VisualizaCartelasContract.View mView;

    private final AppDataSource mAppDataSource;
    private final BaseSchedulerProvider mScheduleProvider;
    private CompositeDisposable mCompositeDisposable;

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

            List<String> letras = Lists.newArrayList("K", "I", "N", "K", "A");
            List<CartelaPedra> cartelaPedras = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                cartelaPedras.add(new CartelaPedra(1,i,0,0));
            }

            document.add(gerarCartela(0, letras, cartelaPedras));

            document.close();

            mView.realizarDownload(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image gerarCartela(int posicao, List<String> letras, List<CartelaPedra> cartelaPedras)
            throws IOException, BadElementException {
        int linhas = 6;
        int colunas = 5;
        int espacamento = 45;
        int largura = espacamento * colunas;
        int altura = espacamento * linhas;
        int larguraLinha = 2;
        int radiusCirculoCentral = espacamento/5;
        int textPedraSize = espacamento/2 + espacamento/8;
        int textCartelaSize = espacamento/4;
        int espacamentoPadrao = (espacamento - larguraLinha)/2;

        Bitmap cartela = Bitmap.createBitmap(largura, altura + textCartelaSize*2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cartela);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

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

            canvas.drawLine(0,
                    (altura/linhas)*i + paddingLinha,
                    largura,
                    (altura/linhas)*i + paddingLinha,
                    paint);

            if (i <= colunas) {
                if (i == 0 || i == colunas) {
                    inicioColuna = 0;
                } else {
                    inicioColuna = altura/linhas;
                }

                if (i == colunas) paddingLinha = -larguraLinha/2;

                canvas.drawLine((largura/colunas)*i + paddingLinha,
                        inicioColuna,
                        (largura/colunas)*i + paddingLinha,
                        altura,
                        paint);
            }
        }

        paint.setAntiAlias(true);
        paint.setTextSize(textPedraSize);
        int espacamentoX;
        int espacamentoY;

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
                    canvas.drawCircle((largura/colunas)*i + espacamento/2,
                            (altura/linhas)*(j + 1) + espacamento/2,
                            radiusCirculoCentral,
                            paint);
                } else {
                    canvas.drawText(StringUtils.formatarNumeroPedra(
                                cartelaPedras.get(contadorPedras).getPedraId()),
                            (largura/colunas)*i + espacamentoX,
                            (altura/linhas)*(j + 1) + espacamentoY,
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
                    (largura/colunas)*i + espacamentoX,
                    espacamentoY,
                    paint);
        }

        paint.setColor(Color.BLACK);
        paint.setTextSize(textCartelaSize);
        String textoCartela = "Cartela: " + CartelaUtils.formatarNumeroCartela(
                cartelaPedras.get(0).getCartelaId());
        Rect r = new Rect();
        paint.getTextBounds(textoCartela, 0, textoCartela.length(), r);
        canvas.drawText(textoCartela,
                largura - r.width() - textCartelaSize/2,
                altura + r.height() + larguraLinha,
                paint);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cartela.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return Image.getInstance(stream.toByteArray());
    }
}
