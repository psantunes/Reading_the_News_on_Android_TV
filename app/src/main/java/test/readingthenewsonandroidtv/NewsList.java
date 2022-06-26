package test.readingthenewsonandroidtv;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import test.readingthenewsonandroidtv.model.Favorite;

public final class NewsList {
    private static final String TAG = "NewsList";
    private static List<News> list;
    private static int count = 0;

    public static List<News> getNewsList() {
        if (list == null) {
            // For this demo, I indexed 3 news manually in method setupNewsManual
            // For use with Firebase, use the method setupNewsOnline() and create
            // your own content in Realtime Database
            list = setupNewsOnline();
        }
        return list;
    }

    public static List<News> setupNewsOnline() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("news");
        list = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    News news = ds.getValue(News.class);
                    list.add(news);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return list;
    }

    public static List<News> setupNewsManual() {
        list = new ArrayList<>();

        News news0 = new News();
        news0.setId(count++);
        news0.setTitle("Síndromes gripais pressionam hospitais de Porto Alegre");
        news0.setArticle("Mais de 430 pacientes aguardavam internações em Porto Alegre nesta terça-feira, em um cenário de emergências e hospitais da Capital superlotados. A situação, que compromete também as consultas eletivas ou de menor gravidade, é causada em especial por síndromes gripais, segundo o diretor Atenção Hospitalar e Urgências da SMS, Francisco Isaías.");
        news0.setSource("Matinal Jornalismo");
        news0.setLink("https://www.matinaljornalismo.com.br/matinal/newsletter/sindromes-gripais-pressionam-hospitais-de-porto-alegre-deixando-mais-de-400-a-espera-por-internacao/");
        news0.setCardImageUrl("https://www.jornalismodigital.jor.br/wp-content/uploads/2022/06/pucrs_p.jpg");
        news0.setBgImageUrl("https://www.jornalismodigital.jor.br/wp-content/uploads/2022/06/pucrs_g.jpg");
        news0.setPhotoCredit("Bruno Todeschini/Divulgação PUCRS");
        list.add(news0);

        News news1 = new News();
        news1.setId(count++);
        news1.setTitle("Caetano, Gil e outros ícones da cultura assinam Carta ao Futuro da Cultura Brasileira");
        news1.setArticle("Artistas da música e das artes cênicas enviaram nesta segunda-feira uma carta aos parlamentares do Congresso Nacional pedindo a derrubada dos vetos da Lei Aldir Blanc 2 e da Lei Paulo Gustavo. A sessão conjunta acontecerá no dia 5 de julho. O texto é assinado por múscios como Alceu Valença, Caetano Veloso, Chico Buarque e Gilberto Gil, atores como Fernanda Montenegro e Emiliano Queiroz, o cineasta Luiz Carlos Barreto e o escritor Zuenir Ventura.");
        news1.setSource("Nonada");
        news1.setLink("https://www.nonada.com.br/2022/06/caetano-gil-e-outros-icones-da-musica-assinam-carta-ao-futuro-da-cultura-brasileira/");
        news1.setCardImageUrl("https://www.jornalismodigital.jor.br/wp-content/uploads/2022/06/gil_p.jpg");
        news1.setBgImageUrl("https://www.jornalismodigital.jor.br/wp-content/uploads/2022/06/gil_g.jpg");
        news1.setPhotoCredit("Hallit/Gilberto Gil Press");
        list.add(news1);

        News news2 = new News();
        news2.setId(count++);
        news2.setTitle("Inter vence Goiás fora de casa e encosta nos líderes do Brasileirão");
        news2.setArticle("O Inter comprovou que está embalado no Brasileirão. Com gols de Edenilson e Alan Patrick, o Colorado venceu o Goiás por 2 a 1 na noite desta quarta-feira, no estádio da Serrinha, pela 12ª rodada. O resultado mantém o time de Mano Menezes (foto) no compasso dos líderes da Série A: são 16 jogos sem perder, uma terceira colocação e 21 pontos somados. A distância para o líder Palmeiras é de apenas um ponto. Na próxima rodada, o Inter encara o Botafogo, às 18h, no estádio Beira-Rio.");
        news2.setSource("Correio do Povo");
        news2.setLink("https://www.correiodopovo.com.br/esportes/inter/inter-vence-goi%C3%A1s-fora-de-casa-e-segue-no-compasso-dos-l%C3%ADderes-do-brasileir%C3%A3o-1.840692");
        news2.setCardImageUrl("https://www.jornalismodigital.jor.br/wp-content/uploads/2022/06/inter_p.jpg");
        news2.setBgImageUrl("https://www.jornalismodigital.jor.br/wp-content/uploads/2022/06/inter_g.jpg");
        news2.setPhotoCredit("Ricardo Duarte/Internacional");
        list.add(news2);

        return list;
    }

}