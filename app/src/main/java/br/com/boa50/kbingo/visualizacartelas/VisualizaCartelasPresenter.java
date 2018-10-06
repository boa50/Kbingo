package br.com.boa50.kbingo.visualizacartelas;

import android.os.Environment;
import android.support.annotation.NonNull;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import br.com.boa50.kbingo.data.AppDataSource;
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
    public void exportarCartelas(int idInicial, int idFinal, File fileDir) {
        if (idInicial > idFinal) {
            mView.mostrarMensagensIdsIncompativeis();
        }

        Document document = new Document();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"test.pdf");
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Android School");
            document.addCreator("Pratik Butani");

//            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
//            float mHeadingFontSize = 20.0f;
//            float mValueFontSize = 26.0f;

//            BaseFont urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

//            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);

// Creating Chunk
            Chunk mOrderDetailsTitleChunk = new Chunk("Order Details"/*, mOrderIdFont*/);

// Creating Paragraph to add...
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);

// Setting Alignment for Heading
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);

// Finally Adding that Chunk
            document.add(mOrderDetailsTitleParagraph);

//            Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderIdChunk = new Chunk("Order No:"/*, mOrderIdFont*/);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();

            mView.realizarDownload(file);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO implementar a lógica da exportação de cartelas
    }
}
