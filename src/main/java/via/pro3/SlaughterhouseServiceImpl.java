package via.pro3;

import io.grpc.stub.StreamObserver;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class SlaughterhouseServiceImpl extends SlaughterhouseServiceGrpc.SlaughterhouseServiceImplBase {

    @Override
    public void getAnimalInfo(Sluaghterhouse.ProductRequest request, StreamObserver<Sluaghterhouse.AnimalResponse> responseObserver) {
        try (Connection conn = DatabaseUtil.getConnection()) {

            System.out.println("Connection established: " + conn.getMetaData().getURL());


            ResultSet searchPathRs = conn.createStatement().executeQuery("SHOW search_path;");
            if (searchPathRs.next()) {
                System.out.println("Current search path: " + searchPathRs.getString(1));
            }


            conn.createStatement().execute("SET search_path TO slaughterhouse");


            String sql = "SELECT id, registration_number, type FROM slaughterhouse.animal WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, request.getProductId());
            ResultSet rs = stmt.executeQuery();


            if (!rs.isBeforeFirst()) {
                System.out.println("No animal found with ID: " + request.getProductId());
                responseObserver.onError(new Exception("No animal found with product_id: " + request.getProductId()));
                return;
            }


            Sluaghterhouse.AnimalResponse.Builder responseBuilder = Sluaghterhouse.AnimalResponse.newBuilder();

            while (rs.next()) {
                Sluaghterhouse.Animal animal = Sluaghterhouse.Animal.newBuilder()
                        .setId(rs.getInt("id"))
                        .setRegistrationNumber(rs.getString("registration_number"))
                        .setType(rs.getString("type"))
                        .build();
                responseBuilder.addAnimals(animal);
            }


            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (SQLException e) {

            System.err.println("SQL Exception: " + e.getMessage());
            responseObserver.onError(new Exception("An SQL error occurred: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(new Exception("An error occurred while fetching animal info: " + e.getMessage()));
        }
    }

    @Override
    public void getProductInfo(Sluaghterhouse.AnimalRequest request, StreamObserver<Sluaghterhouse.ProductResponse> responseObserver) {
        try (Connection conn = DatabaseUtil.getConnection()) {

            System.out.println("Connection established: " + conn.getMetaData().getURL());


            ResultSet searchPathRs = conn.createStatement().executeQuery("SHOW search_path;");
            if (searchPathRs.next()) {
                System.out.println("Current search path: " + searchPathRs.getString(1));
            }


            conn.createStatement().execute("SET search_path TO slaughterhouse");


            String sql = "SELECT id, animal_reference, parts FROM product WHERE id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, request.getAnimalId());
            ResultSet rs = stmt.executeQuery();


            if (!rs.isBeforeFirst()) {
                System.out.println("No product found with ID: " + request.getAnimalId());
                responseObserver.onError(new Exception("No product found with animal_id: " + request.getAnimalId()));
                return;
            }


            Sluaghterhouse.ProductResponse.Builder responseBuilder = Sluaghterhouse.ProductResponse.newBuilder();

            while (rs.next()) {
                Sluaghterhouse.Product product = Sluaghterhouse.Product.newBuilder()
                        .setId(rs.getInt("id"))
                        .setAnimalReference(rs.getString("animal_reference"))
                        .addAllParts(Arrays.asList((String[]) rs.getArray("parts").getArray()))
                        .build();
                responseBuilder.addProducts(product);
            }


            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        } catch (SQLException e) {

            System.err.println("SQL Exception: " + e.getMessage());
            responseObserver.onError(new Exception("An SQL error occurred: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onError(new Exception("An error occurred while fetching product info: " + e.getMessage()));
        }
    }
}
