package com.szantog.brew_e;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlogFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView shopNameTextView = view.findViewById(R.id.blog_shop_name);
        shopNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = new String[]{"A kávé története", "Kávéházak magyarországgon", "Hogyan pörkölünk?"};
                new AlertDialog.Builder(getActivity())
                        .setTitle("További cikkek - Espresso Embassy")
                        .setItems(items, null)
                        .show();
            }
        });

        String ex = "A kávé története\n" +
                "Gondoltad volna, hogy valószínűleg a kávét eleinte nem itták, hanem ették? Az ugandaiak például, ha távoli útra indultak, szárított babszemeket rágcsáltak, mivel úgy hitték, így különleges erőre kapnak. De milyen messzire is nyúlik vissza a kávé története?\n" +
                "\n" +
                " \n" +
                "\n" +
                "Az első kávészállítmány VelencébenAz egyik legenda szerint a kávészemek frissítő hatását egy Káldi nevű etióp pásztor fedezte fel, aki észrevette, hogy ha a kecskéi piros bogyókat legelésznek, sokkal élénkebbek lesznek. Ezt elmondta a közelben élő szerzeteseknek, akik rájöttek arra, hogy ha a magokat megpörkölik, ízletes italt készíthetnek.\n" +
                "\n" +
                "A másik történet szerint egy Rhazes nevű arab orvos a kávét, a \"quawa\" nevű élénkítő növényt orvosságként használta, és meg is említette A kontinens című munkájában.\n" +
                "\n" +
                "A kávét először Jemenben kezdték el termeszteni, ahol teraszos gazdálkodást folytattak. A kávézás szokását mohamedán zarándokok vitték magukkal Mekkába és Medinába, innen terjedt tovább a kávé fogyasztása az egész Közel-Keletre. Később a kávétermesztés megjelent Arábiában és Egyiptomban is, ahol a kávé (vagy Kahweh) fogyasztása nem sokkal később mindennapi szokássá vált.\n" +
                "\n" +
                "A 16. században a Közel-Keleten járó utazók és botanikusok egy eddig ismeretlen növényről és annak terméséből készült italról küldtek híreket Európába. A kereskedők hamar felismerték az újdonságban rejlő lehetőségeket, és az 1600-as évek elején megérkeztek az első kávészemekkel töltött zsákok Velencébe. Ez volt az a pillanat, amikor az európai emberek elkezdtek megismerkedni a kávéval. A velenceiek kávészállítmányának híre gyorsan elterjedt, így nem sokkal később a holland kereskedők is elkezdtek érdeklődni a kávé termesztése és kereskedelmi forgalmazása iránt. Az európai utazóknak és szerzeteseknek köszönhetően hamarosan a kávé a világ minden tájára eljutott, és népszerűsége hihetetlen gyorsan növekedett.\n" +
                "\n" +
                "A hirtelen jött hírnév jó alapot biztosított egy új társasági színtér megalakulásának, ezért Európa-szerte (Olaszországban, Nagy-Britanniában, Hollandiában, Franciaországban és Németországban) egymásra nyíltak meg a kávéházak. Mint ahogy az első kávészállítmány is Velencében ért földet Európában, úgy az öreg kontinens első kávéháza is itt nyitotta ki kapuit La Bottega del Caffé néven 1624-ben. A 18. század elejére a hollandok kávéültetvényeket létesítettek Indonéziában, és a franciák elvittek néhány ültetvényt Martinique-ra, míg a spanyolok a Karib-tenger térségében, Közép-Amerikában és Brazíliában kezdtek kávét termeszteni. Így jutott Brazília kávéhoz, és ennek eredményeképp napjainkra Brazília vált a világ egyik legnagyobb kávétermesztő országává. A németek eljuttatták a kávét Kelet-Afrikába, Kenyába és a Kilimandzsáró lejtőire. A 19. századra a Ráktérítő és a Baktérítő sávjában a kávé egyenletesen elterjedt, majd a 20. századra Európa–szerte és a világ több pontján is közkedvelt élvezeti cikké vált. ";

        TextView blogText = view.findViewById(R.id.blog_main);
        blogText.setMovementMethod(new ScrollingMovementMethod());
        blogText.setText(Html.fromHtml(ex));
    }
}
