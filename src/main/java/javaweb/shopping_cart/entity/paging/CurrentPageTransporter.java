package javaweb.shopping_cart.entity.paging;

public class CurrentPageTransporter {
    private static int currentPage;

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(int currentPage) {
        CurrentPageTransporter.currentPage = currentPage;
    }
}
