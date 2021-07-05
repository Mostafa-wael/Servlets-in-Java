import java.io.*;
// import java.lang.Math;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class matrixServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // get the inputs from the frontend
        String mat = request.getParameter("matrix");
        String trans = request.getParameter("transpose");
        String det = request.getParameter("determinant");

        // trim the input
        String[] matStr = mat.trim().replaceAll("\\s+", " ").replaceAll("(\\r|\\n)", " ").split(" ");

        // create the matrix
        int[] matArr = new int[matStr.length];
        for (int i = 0; i < matStr.length; i++) {
            matArr[i] = Integer.parseInt(matStr[i]);
        }
        Matrix recMatrix = new Matrix(matArr);

        // fill the frontend
        response.setContentType("text/html");

        String page = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n <title>Results </title>\n"
                + "</head> <body style=\"background-color: rgba(133, 133, 11,0.4);\">\n</head><body>\n"
                + "    <h1 style=\"text-align: center; color:indigo; font-family:'Comic Sans MS';\">Result</h1>\n"
                + "    <h2 style=\"text-align: center; color:indigo; font-family:'Comic Sans MS';\">Original Matrix</h2>\n";
        page += recMatrix.embeddMatrixToPage();

        if (trans != null && det != null) {
            page += "<h2 style=\"text-align: center; color:indigo; font-family:'Comic Sans MS';\">Transpose</h2>\n";
            page += recMatrix.transpose().embeddMatrixToPage();
            page += "<h2 style=\"text-align: center; color:indigo; font-family:'Comic Sans MS';\">Determinant is </h2>\n <div style=\"text-align: center; height:auto; width:auto;\"><h2 style=\"text-align: center; color:black; font-family:'Comic Sans MS'\">"
                    + Integer.toString(recMatrix.determinant(recMatrix.elements));
            page += "</p>\n</div>";

        } else if (trans != null) {

            page += "<h2 style=\"text-align: center; color:indigo; font-family:'Comic Sans MS';\">Transpose</h2>\n";
            page += recMatrix.transpose().embeddMatrixToPage();

        } else if (det != null) {
            page += "<div style=\"text-align: center; height:auto; width:auto; background-color:rgba(133, 133, 11,0.4);\">";
            page += "<h2 style=\"text-align: center; color:indigo; font-family:'Comic Sans MS';\">Determinant is </h2>\n <div style=\"text-align: center; height:auto; width:auto; \"><h2 style=\"text-align: center; color:black; font-family:'Comic Sans MS';\">"
                    + Integer.toString(recMatrix.determinant(recMatrix.elements));
            page += "</p>\n</div>";
        }
        page += "</body>\n" + "</html>";
        response.getWriter().println(page);
    }

    private class Matrix {
        public int elements[][]; // 2D array to carry the elements
        public int size; // matrix size

        public Matrix(int matrix[]) // fill the matrix with the passed values
        {
            size = (int) Math.sqrt(matrix.length); // the size of the matrix equals the saure root if the passed array
            elements = new int[size][size];
            int count = 0;
            for (int i = 0; i < this.size; i++)
                for (int j = 0; j < this.size; j++)
                    this.elements[i][j] = matrix[count++]; // convert the 1D array to a 2D array
        }

        public Matrix transpose() {
            int matrix[] = new int[this.size * this.size];
            Matrix trans = new Matrix(matrix);// use a new matrix to avoid the matrix overwrites its own elemnets
            for (int i = 0; i < this.size; i++)
                for (int j = 0; j < this.size; j++)
                    trans.elements[i][j] = this.elements[j][i]; // just swuitch elemnets
            return trans;
        }

        public int determinant(int[][] matrix) {
            // base cases
            if (matrix.length == 1) // 1D matrix
                return matrix[0][0];

            if (matrix.length == 2) // 2D matrix
                return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            // acctual calculations
            int det = 0;
            for (int i = 0; i < matrix[0].length; i++) {
                int tempMat[][] = new int[matrix.length - 1][matrix[0].length - 1];
                for (int j = 1; j < matrix.length; j++) {
                    for (int k = 0; k < matrix[0].length; k++)
                        if (k < i)
                            tempMat[j - 1][k] = matrix[j][k];
                        else if (k > i)
                            tempMat[j - 1][k - 1] = matrix[j][k];
                }
                // we can use this function "Math.pow(-1, (int) i)" instead of ""(i % 2 == 0 ? 1
                // : -1)"
                det += matrix[0][i] * (i % 2 == 0 ? 1 : -1) * determinant(tempMat);
            }
            return det;
        }

        public String embeddMatrixToPage() {
            String page = "<div style=\"text-align: center; height:auto; width:auto; \">";
            for (int i = 0; i < this.size; i++) {
                String newRow = "";
                for (int j = 0; j < this.size; j++)
                    newRow = newRow + Integer.toString(this.elements[i][j]) + " ";
                page += "<p style=\"text-align: center;  color:black; font-family:'Comic Sans MS'; font-size:larger;\">"
                        + newRow + "</p>\n";
            }
            return page + "</div>";
        }
    }
}
