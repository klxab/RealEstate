public class RealEstateTest {

    public interface PropertyInterface {
        void makeDiscount(int percentage);
        int getTotalPrice();
        double averageSqmPerRoom();
        String toString();
    }


    public interface PanelInterface {
        boolean hasSameAmount(RealEstate other);
        int roomPrice();
    }
    public static class RealEstate implements PropertyInterface {

        protected String city;
        protected double price;
        protected int sqm;
        protected double numberOfRooms;
        protected Genre genre;

        public enum Genre {
            FAMILYHOUSE,
            CONDOMINIUM,
            FARM
        }

        public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
            this.city = city;
            this.price = price;
            this.sqm = sqm;
            this.numberOfRooms = numberOfRooms;
            this.genre = genre;
        }

        @Override
        public void makeDiscount(int percentage) {
            this.price = this.price * (1 - percentage / 100.0);
        }

        @Override
        public int getTotalPrice() {
            double total = price * sqm;
            switch (city) {
                case "Budapest": total *= 1.3; break;
                case "Debrecen": total *= 1.2; break;
                case "Nyíregyháza": total *= 1.15; break;
            }
            return (int) total;
        }

        @Override
        public double averageSqmPerRoom() {
            return sqm / numberOfRooms;
        }

        @Override
        public String toString() {
            return "RealEstate [city=" + city + ", price per sqm=" + price + ", sqm=" + sqm +
                    ", numberOfRooms=" + numberOfRooms + ", genre=" + genre +
                    ", totalPrice=" + getTotalPrice() +
                    ", avgSqmPerRoom=" + averageSqmPerRoom() + "]";
        }
    }
    public static class Panel extends RealEstate implements PanelInterface {

        private int floor;
        private boolean isInsulated;

        public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre, int floor, boolean isInsulated) {
            super(city, price, sqm, numberOfRooms, genre);
            this.floor = floor;
            this.isInsulated = isInsulated;
        }

        @Override
        public int getTotalPrice() {
            double total = price * sqm;
            switch (city) {
                case "Budapest": total *= 1.3; break;
                case "Debrecen": total *= 1.2; break;
                case "Nyíregyháza": total *= 1.15; break;
            }
            if (floor >= 0 && floor <= 2) total *= 1.05;
            if (floor == 10) total *= 0.95;
            // Insulation adjustment
            if (isInsulated) total *= 1.05;
            return (int) total;
        }

        @Override
        public boolean hasSameAmount(RealEstate other) {
            return this.getTotalPrice() == other.getTotalPrice();
        }

        @Override
        public int roomPrice() {
            return (int) (price * sqm / numberOfRooms);
        }

        @Override
        public String toString() {
            return "Panel [city=" + city + ", price per sqm=" + price + ", sqm=" + sqm +
                    ", numberOfRooms=" + numberOfRooms + ", genre=" + genre +
                    ", floor=" + floor + ", insulated=" + isInsulated +
                    ", totalPrice=" + getTotalPrice() +
                    ", avgSqmPerRoom=" + averageSqmPerRoom() + "]";
        }
    }

    public static void testRealEstateCreation() {
        RealEstate house = new RealEstate("Tokaj", 7000, 350, 3, RealEstate.Genre.FARM);
        System.out.println("=== Test 1: RealEstate Creation ===");
        System.out.println(house);
    }

    public static void testPanelCreation() {
        Panel panel = new Panel("Kalmanhaza", 1500, 60, 3, RealEstate.Genre.CONDOMINIUM, 6, false);
        System.out.println("=== Test 2: Panel Creation ===");
        System.out.println(panel);
    }

    public static void testPanelDiscount() {
        Panel panel = new Panel("Tokaj", 7000, 350, 3, RealEstate.Genre.CONDOMINIUM, 3, true);
        panel.makeDiscount(25);
        System.out.println("=== Test 3: Panel Discount ===");
        System.out.println(panel);
        System.out.println("Room price: " + panel.roomPrice());
    }

    public static void main(String[] args) {
        testRealEstateCreation();
        testPanelCreation();
        testPanelDiscount();
    }
}
