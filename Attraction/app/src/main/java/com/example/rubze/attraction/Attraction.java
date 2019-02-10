package com.example.rubze.attraction;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Attraction implements Comparable<Attraction>, Serializable {

    private String name, area, zone, municipality, info, percentage;
    private boolean beach, city, country, central;
    private int image, imageMini, sand, popular, touristic, cultural, tradition, free, hospitality, inFamily,
            overnight, fastVisit, closingTime, park, minimumServices, roadServices,
            publicTransport, directCarArrival, arrivalDifficulty, nightLife, recreational,
            mountainSports, waterSports, punctuation;

    private ArrayList<Integer> levels, imageIdArray;
    private ArrayList<Integer> levelsForImages, legendStringId;


    public Attraction(int image, int imageMini, String name, String area, String zone,
                      String municipality, String info, boolean beach, boolean city, boolean country,
                      boolean central, int sand, int popular, int touristic, int cultural,
                      int tradition, int free, int hospitality, int inFamily, int overnight,
                      int fastVisit, int closingTime, int park, int minimumServices, int roadServices,
                      int publicTransport, int directCarArrival, int arrivalDifficulty, int nightLife,
                      int recreational, int mountainSports, int waterSports) {

        this.image=image; this.imageMini=imageMini; this.name=name; this.area=area; this.zone=zone;
        this.municipality=municipality;this.info=info; this.beach=beach; this.city=city;
        this.country=country; this.central=central; this.sand=sand; this.popular=popular;
        this.touristic=touristic; this.cultural=cultural;this.tradition=tradition; this.free=free;
        this.hospitality=hospitality;this.inFamily=inFamily; this.overnight=overnight;
        this.fastVisit=fastVisit;this.closingTime=closingTime; this.park=park;
        this.minimumServices=minimumServices;this.roadServices=roadServices;
        this.publicTransport=publicTransport;this.directCarArrival=directCarArrival;
        this.arrivalDifficulty=arrivalDifficulty; this.nightLife=nightLife;
        this.recreational=recreational; this.mountainSports=mountainSports;
        this.waterSports=waterSports;

        this.punctuation=0;
        levels = new ArrayList<>();
        inflateArrayList();
    }

    public String getName() { return name; }
    public String getArea() { return area; }
    public String getZone() { return zone; }
    public String getInfo() { return info; }

    public int getImage() { return image; }
    public int getImageMini() { return imageMini; }
    public String getMunicipality() { return municipality; }
    public boolean isBeach() { return beach; }
    public int getPopular() { return popular; }
    public int getFree() { return free; }
    public int getHospitality() { return hospitality; }
    public int getInFamily() { return inFamily; }
    public int getClosingTime() { return closingTime; }
    public int getMinimumServices() { return minimumServices; }
    public int getPublicTransport() { return publicTransport; }
    public int getNightLife() { return nightLife; }

    public int getPunctuation() { return punctuation; }
    public void setPunctuation(int punctuation) { this.punctuation = punctuation; }

    public String getPercentage() { return percentage; }
    public void setPercentage(String percentage){ this.percentage = percentage;}

    private void inflateArrayList() {
        levels.add(sand);levels.add(popular);levels.add(touristic); levels.add(cultural);
        levels.add(tradition); levels.add(free); levels.add(hospitality); levels.add(inFamily);
        levels.add(overnight); levels.add(fastVisit); levels.add(closingTime); levels.add(park);
        levels.add(minimumServices); levels.add(roadServices);levels.add(publicTransport);
        levels.add(directCarArrival); levels.add(arrivalDifficulty); levels.add(nightLife);
        levels.add(recreational); levels.add(mountainSports); levels.add(waterSports);

        declareLists();
        addLevelsForImages();
    }

    private void declareLists(){
        imageIdArray = new ArrayList<>();
        levelsForImages = new ArrayList<>();
        legendStringId = new ArrayList<>();
    }

    public void addIcon(int id){
        imageIdArray.add(id);
    }
    public void addLegend(int id) {legendStringId.add(id);}

    private void addLevelsForImages(){
        levelsForImages.add(getFree());
        levelsForImages.add(getHospitality());
        levelsForImages.add(getInFamily());
        levelsForImages.add(getNightLife());
        levelsForImages.add(getClosingTime());
        levelsForImages.add(getPublicTransport());
        levelsForImages.add(getMinimumServices());
        levelsForImages.add(getPopular());
    }

    public void createPercentage(){
        int per = (punctuation*100)/(levels.size()*3);
        percentage = per+"%";
    }

    public ArrayList<Integer> getDrawableIconArray(){
        return imageIdArray;
    }
    public ArrayList<Integer> getLevels() {
        return levels;
    }
    public ArrayList<Integer> getLevelsForImages() { return levelsForImages;}
    public ArrayList<Integer> getLegendStringId() {return legendStringId;}

    @Override
    public int compareTo(@NonNull Attraction o) {
        return this.getName().compareTo(o.getName());
    }
}
