package es.upm.dit.apsv.transportationorderserver;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import es.upm.dit.apsv.transportationorderserver.repository.TransportationOrderRepository;
import es.upm.dit.apsv.transportationorderserver.controller.TransportationOrderController;
import es.upm.dit.apsv.transportationorderserver.model.TransportationOrder;

@WebMvcTest(TransportationOrderController.class)
public class TransportationOrderControllerTest {
  @InjectMocks
  private TransportationOrderController business;
  @MockBean
  private TransportationOrderRepository repository;
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testGetOrders() throws Exception {
    // call GET "/transportationorders" application/json

    when(repository.findAll()).thenReturn(getAllTestOrders());
    RequestBuilder request = MockMvcRequestBuilders
        .get("/transportationorders")
        .accept(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(20)))
        .andReturn();
  }

  private List<TransportationOrder> getAllTestOrders() {
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<TransportationOrder> orders = new ArrayList<TransportationOrder>();
    TransportationOrder order = null;

    try (BufferedReader br = new BufferedReader(new FileReader(
        new ClassPathResource("orders.json").getFile()))) {
      for (String line; (line = br.readLine()) != null;) {
        order = objectMapper.readValue(line, TransportationOrder.class);
        orders.add(order);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return orders;
  }

  @Test

public void testGetOrder() throws Exception {

     //call GET "/transportationorders/{truck}"  application/json

       

     when(repository.findById("8962ZKR")).thenReturn(Optional.of(

              new TransportationOrder("28","8962ZKR",1591682400000L,

              40.4562191,-3.8707211,1591692196000L,42.0206372,-4.5330132,

              0,0.0,0.0,0)));

              RequestBuilder request = MockMvcRequestBuilders
              .get("/transportationorders/8962ZKR")
              .accept(MediaType.APPLICATION_JSON);
        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.toid").value("28"))
                .andExpect(jsonPath("$.truck").value("8962ZKR"))
                .andExpect(jsonPath("$.originDate").value(1591682400000L))
                .andExpect(jsonPath("$.originLat").value(40.4562191))
                .andExpect(jsonPath("$.originLong").value(-3.8707211))
                .andExpect(jsonPath("$.dstDate").value(1591692196000L))
                .andExpect(jsonPath("$.dstLat").value(42.0206372))
                .andExpect(jsonPath("$.dstLong").value(-4.5330132))
                .andExpect(jsonPath("$.lastDate").value(0))
                .andExpect(jsonPath("$.lastLat").value(0.0))
                .andExpect(jsonPath("$.lastLong").value(0.0))
                .andExpect(jsonPath("$.st").value(0));

                verify(null)

}

@Test
    public void testGetOrderNotFound() throws Exception {
        // Arrange
        String nonExistingTruckId = "NADA";
        when(repository.findById(nonExistingTruckId)).thenReturn(Optional.empty());

        // Act & Assert for non-existing order
        mockMvc.perform(get("/transportationorders/{truck}", nonExistingTruckId)
                .contentType("application/json"))
                .andExpect(status().isNotFound());

        // Verify
        verify(repository, times(1)).findById(nonExistingTruckId);
    }
}