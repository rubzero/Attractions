package com.example.rubze.attraction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class AttractionList implements Serializable {

    private ArrayList<Attraction> attractionList;
    private DrawableIconList drawableIconList;
    private int[] legendString={R.string.legendBeach, R.string.legendMountain, R.string.legendMoney,
    R.string.legendHotel, R.string.legendInFamily, R.string.legendNightLife, R.string.legendNotClosing,
    R.string.legendPublicTransport, R.string.legendShopping, R.string.legendPopular};

    private Attraction[] attractionArray = {
            new Attraction(R.drawable.vegueta, R.drawable.vegueta_mini,"Casco antigüo de Vegueta",
                    "Urbano","Norte","Las Palmas",
                    "http://cabildo.grancanaria.com/tamadaba", false, true,
                    false,true, 0,3,3,3,3,
                    3,3,3,3,2,1,2,
                    3, 3,3,3,1,
                    3,0, 0,0),
            new Attraction(R.drawable.nublo,R.drawable.nublo_mini,"El Roque Nublo",
                    "Montaña","Centro","Tejeda",
                    "http://cabildo.grancanaria.com/tamadaba",false, false,
                    true, false, 0,3,2,2,2,
                    3,1,3, 1,1,2,1,
                    2,2,2,2, 2,
                    1,1,2,0),
            new Attraction(R.drawable.dunas,R.drawable.dunas_mini,"Dunas de Maspalomas",
                    "Costa","Sur", "Maspalomas",
                    "http://cabildo.grancanaria.com/tamadaba",true, false,
                    false,true, 3, 3,3,1,1,
                    3,3,3, 3, 2,1,3,
                    3,3, 3,3, 1,
                    3,0, 0, 2),
            new Attraction( R.drawable.castillo_luz,R.drawable.castillo_luz_mini,
                    "Castillo de la Luz","Urbano","Norte",
                    "Las Palmas", "http://cabildo.grancanaria.com/tamadaba",
                    false, true, false,true, 0,2,
                    2,2,2, 3,3,3, 2,
                    3,2,2, 3,3, 3,
                    2,1, 2, 2, 0,
                    0),
            new Attraction(R.drawable.canteras, R.drawable.canteras_mini,"Playa de las Canteras",
                    "Costa","Norte", "Las Palmas",
                    "http://cabildo.grancanaria.com/tamadaba", true,false,
                    false,true, 3,3,3,2,1,
                    3,3,3, 3,2,1,2,
                    3,3, 3,3,1,
                    3,2, 1, 3),
            new Attraction(R.drawable.agaete, R.drawable.agaete_mini,"Puerto de Agaete",
                    "Costa","Norte", "Agaete",
                    "http://cabildo.grancanaria.com/tamadaba", true,true,
                    false,false, 1,3,3,2,2,
                    3,2,3, 1,1,1,1,
                    3,2, 2,3,1,
                    1,1, 2, 2),
            new Attraction(R.drawable.azuaje, R.drawable.azuaje_mini,"Barranco de Azuaje",
                    "Montaña","Centro", "Firgas",
                    "http://cabildo.grancanaria.com/tamadaba",
                    false,false ,true,false, 1,1,1,
                    2,2,3,1,2, 1,1,
                    1,1,2,2, 1,
                    2,3,1,2, 2,
                    1),
            new Attraction(R.drawable.bandama, R.drawable.bandama_mini,"Caldera de Bandama",
                    "Montaña","Centro", "Santa Brígida",
                    "http://cabildo.grancanaria.com/tamadaba", false,false ,
                    true,false, 1,2,1,2,1,
                    3,1,3, 3,1,3,2,
                    2,2, 2,1,2,
                    1,3, 2, 1),
            new Attraction(R.drawable.barranco_cercicalos, R.drawable.barranco_cernicalos_mini,
                    "Barranco de los Cernícalos","Montaña","Sureste", "Telde",
                    "http://cabildo.grancanaria.com/tamadaba", false,false ,
                    true,false, 1,2,2,2,2,
                    3,1,2, 1,1,2,1,
                    2,2, 1,2,3,
                    1,2, 1, 1),
            new Attraction(R.drawable.catedral_arucas, R.drawable.catedral_arucas_mini,
                    "Catedral de Arucas","Ciudad","Centro", "Arucas",
                    "http://cabildo.grancanaria.com/tamadaba", false,true ,
                    true,false, 1,3,3,2,3,
                    3,2,3, 2,2,1,2,
                    3,3, 3,3,2,
                    2,1, 1, 1),
            new Attraction(R.drawable.charco_azul, R.drawable.charco_azul_agaete,
                    "Charco Azul", "Montaña","Norte", "Agaete",
                    "http://cabildo.grancanaria.com/tamadaba", false,true ,
                    true,false, 1,3,2,1,2,
                    3,1,1, 1,1,1,1,
                    2,2, 1,1,3,
                    1,2, 1, 1),
            new Attraction(R.drawable.faro_maspalomas, R.drawable.faro_maspalomas_minii,
                    "Faro de Maspalomas","Costa","Sur", "Maspalomas",
                    "http://cabildo.grancanaria.com/tamadaba", true,true ,
                    false,false, 3,2,3,1,2,
                    3,3,3, 2,3,1,3,
                    3,3, 2,2,1,
                    3,1, 1, 3),
            new Attraction(R.drawable.playa_ingles, R.drawable.playa_ingles_mini,
                    "Playa del Inglés","Costa","Sur", "Maspalomas",
                    "http://cabildo.grancanaria.com/tamadaba", true,true ,
                    false,false,3,2, 3,1,2,
                    3,3,3, 2, 3,1,3,
                    3,3, 3, 3,1,
                    3,2, 1, 3),
            new Attraction(R.drawable.presa_ninas, R.drawable.presa_ninas_mini,
                    "Presa de las niñas",
                    "Montaña","Centro", "Tejeda",
                    "http://cabildo.grancanaria.com/tamadaba", false,false ,
                    true,false,1,2,3, 2,2,
                    3,1,3,3,1, 1,1,
                    2,3, 2, 3,3,
                    1,3, 3, 1),
            new Attraction(R.drawable.puerto_mogan, R.drawable.puerto_mogan_mini,
                    "Puerto de Mogán","Costa","Sur", "Mogán",
                    "http://cabildo.grancanaria.com/tamadaba", true,true ,
                    false,false, 2,2, 3,2,2,
                    3,3,3, 1, 1,1,2,
                    3,3, 2, 3,
                    2,3,2, 2, 3),
            new Attraction(R.drawable.santa_ana, R.drawable.santa_ana_minii,
                    "Catedral de Santa Ana","Urbano","Norte","Las Palmas",
                    "http://cabildo.grancanaria.com/tamadaba",false,true ,
                    false,true, 1,3,2,3,3,
                    2,3,3, 1,2,3,1,
                    3,3, 3,2,2,
                    3,1, 1, 1)};

    public AttractionList() {
        attractionList = new ArrayList<>();
        drawableIconList = new DrawableIconList();
        addAttractions();
        addAttractionIcons();
    }

    public ArrayList<Attraction> getAttractionList() {
        return attractionList;
    }

    private void addAttractions() {
        for(int i=0; i<attractionArray.length; i++)
            attractionList.add(attractionArray[i]);
        Collections.sort(attractionList);
    }

    private void addAttractionIcons(){
        for(int i=0; i<attractionList.size(); i++){
            if(attractionList.get(i).isBeach()) {
                attractionList.get(i).addIcon(drawableIconList.iconList.get(0));
                attractionList.get(i).addLegend(legendString[0]);
            }
            else {
                attractionList.get(i).addIcon(drawableIconList.iconList.get(1));
                attractionList.get(i).addLegend(legendString[1]);
            }
            for(int j=2; j<drawableIconList.iconList.size();j++){
                if(attractionList.get(i).getLevelsForImages().get(j-2)==3) {
                    attractionList.get(i).addIcon(drawableIconList.iconList.get(j));
                    attractionList.get(i).addLegend(legendString[j]);
                }
            }
        }
    }

    public Attraction getAttractionByImage(int imageId){
        Attraction attraction = null;
        for(int i=0; i<attractionList.size(); i++)
            if(attractionList.get(i).getImage()==imageId)
                attraction = attractionList.get(i);
        return attraction;
    }

    public ArrayList<Integer> getIconList(){
        return drawableIconList.getIconList();
    }

    private static class DrawableIconList implements Serializable{
        private ArrayList<Integer> iconList;

        private DrawableIconList(){
            iconList = new ArrayList<>();
            addIcons();
        }

        private void addIcons(){
            iconList.add(R.drawable.ic_beach);
            iconList.add(R.drawable.ic_mountain);
            iconList.add(R.drawable.ic_money);
            iconList.add(R.drawable.ic_hotel);
            iconList.add(R.drawable.ic_in_family);
            iconList.add(R.drawable.ic_night_life);
            iconList.add(R.drawable.ic_not_closing);
            iconList.add(R.drawable.ic_public_transport);
            iconList.add(R.drawable.ic_shopping);
            iconList.add(R.drawable.ic_popular);
        }

        private ArrayList<Integer> getIconList(){
            return iconList;
        }
    }
}
