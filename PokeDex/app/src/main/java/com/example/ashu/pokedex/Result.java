package com.example.ashu.pokedex;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result{
    @SerializedName("name")
    public String name;
    @SerializedName("weight")
   public String weight;
    @SerializedName("height")
    String height;
    @SerializedName("id")
    String id;
    @SerializedName("order")
    String order;
    @SerializedName("base_experience")
    String baseExperience;

    /**
     * Ability ArrayList to store the ability of individual Pokemon.
     */

    @SerializedName("abilities")
    ArrayList<Ability> abilities;



    class Ability{

        AbilityInner ability = new AbilityInner();
        class AbilityInner{
            String name;

            public String getName() {
                return name;
            }
        }

        public AbilityInner getAbility() {
            return ability;
        }
    }



    /**
     * Stats ArrayList to store stats like hp ,defence points and their name etc;
     */

    @SerializedName("stats")
    ArrayList<Stats> stats;
    class Stats{

        Stat stat = new Stat();

        public Stat getStat() {
            return stat;
        }

        class Stat{
            String name;

            public String getName() {
                return name;
            }
        }

        @SerializedName("base_stat")
        String baseStat;

        public String getBaseStat() {
            return baseStat;
        }

    }


    /**
     * Moves ArrayList to store special attack of Pokemon.
     */

    @SerializedName("moves")
    ArrayList<Moves> moves;
    class Moves{

        Move move = new Move();

        public Move getMove() {
            return move;
        }

        class Move{

            String name;

            public String getName() {
                return name;
            }

        }

    }

    /**
     * Types ArrayList to store type of Pokemon like ground,water,fire etc.
     */

    @SerializedName("types")
    ArrayList<Types> types;
    class Types{

        Type type = new Type();
        String slot;

        public Type getType() {
            return type;
        }

        public String getSlot() {
            return slot;
        }

        class Type{

            String name;

            public String getName() {
                return name;
            }

        }

    }

    /**
     * Sprites Class to store image url of individual Pokemon.
     */


    Sprites sprites = new Sprites();
    class Sprites{
        @SerializedName("front_default")
        String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }
    }





    public Sprites getSprites() {
        return sprites;
    }


    public String getName() {
        return name;
    }


    public String getWeight() {
        return weight;
    }


    public String getHeight() {
        return height;
    }


    public String getId() {
        return id;
    }


    public String getOrder() {
        return order;
    }


    public String getBaseExperience() {
        return baseExperience;
    }


    public ArrayList<Ability> getAbilities() {
        return abilities;
    }


    public ArrayList<Stats> getStats() {
        return stats;
    }


    public ArrayList<Moves> getMoves() {
        return moves;
    }


    public ArrayList<Types> getTypes() {
        return types;
    }

}
