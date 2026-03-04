package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.request.StockEntryRequest;
import rokaly.sca.dto.response.StockEntryResponse;
import rokaly.sca.dto.request.StockExitRequest;
import rokaly.sca.entity.MovementStock;
import rokaly.sca.entity.Position;
import rokaly.sca.entity.Product;
import rokaly.sca.entity.Stock;
import rokaly.sca.repository.MovementStockRepository;
import rokaly.sca.repository.PositionRepository;
import rokaly.sca.repository.ProductRepository;
import rokaly.sca.repository.StockRepository;

import java.util.List;

@Service
public class StockService {
    
    private final StockRepository stockRepository;
    private final MovementStockRepository movementStockRepository;
    private final ProductRepository productRepository;
    private final PositionRepository positionRepository;

    public StockService(StockRepository stockRepository, MovementStockRepository movementStockRepository, ProductRepository productRepository, PositionRepository positionRepository) {
        this.stockRepository = stockRepository;
        this.movementStockRepository = movementStockRepository;
        this.productRepository = productRepository;
        this.positionRepository = positionRepository;
    }

    public ResponseEntity<StockEntryResponse> createEntries(StockEntryRequest data, UriComponentsBuilder uriBuilder) {
        Stock.isValidAmount(data.amount());

        Product product = productRepository.findById(data.productId()).orElseThrow(
                () -> new EntityNotFoundException("Product not found with id: " + data.productId())
        );

        product.isActiveProductStatus(product.getStatus());

        Position position = positionRepository.findById(data.positionId()).orElseThrow(
                () -> new EntityNotFoundException("Position not found with id: " + data.positionId())
        );

        position.isActivePositionStatus(position.getStatus());

        Stock stock = new Stock(product, position, data.amount());
        stockRepository.save(stock);

        MovementStock movementStock = MovementStock.createIn(product.getCode(), product.getName(), position.getCode(), data.amount(), data.name(), data.reason());
        movementStockRepository.save(movementStock);

        var uri = uriBuilder.path("/{id}").buildAndExpand(stock.getId()).toUri();
        StockEntryResponse dto = new StockEntryResponse(stock.getId(), stock.getProduct().getCode() ,stock.getProduct().getName(), stock.getPosition().getCode(), stock.getAmount());

        return ResponseEntity.created(uri).body(dto);
    }

    public ResponseEntity<Page<StockEntryResponse>> getAll(Pageable pagination) {
        Page<StockEntryResponse> stock = stockRepository.findAll(pagination).map(StockEntryResponse::new);
        return ResponseEntity.ok(stock);
    }
}
