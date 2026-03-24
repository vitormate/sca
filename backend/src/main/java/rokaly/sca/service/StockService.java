package rokaly.sca.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import rokaly.sca.dto.request.StockAdjustmentRequest;
import rokaly.sca.dto.request.StockEntryRequest;
import rokaly.sca.dto.response.StockResponse;
import rokaly.sca.entity.MovementStock;
import rokaly.sca.entity.Position;
import rokaly.sca.entity.Product;
import rokaly.sca.entity.Stock;
import rokaly.sca.repository.MovementStockRepository;
import rokaly.sca.repository.PositionRepository;
import rokaly.sca.repository.ProductRepository;
import rokaly.sca.repository.StockRepository;
import rokaly.sca.utils.mappers.MovementStockMapper;

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

    public ResponseEntity<StockResponse> createEntries(StockEntryRequest data, UriComponentsBuilder uriBuilder) {
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

        MovementStock movementStock = MovementStockMapper.createIn(product.getCode(), product.getName(), position.getCode(), data.amount(), data.name(), data.reason());
        movementStockRepository.save(movementStock);

        var uri = uriBuilder.path("/{id}").buildAndExpand(stock.getId()).toUri();
        StockResponse dto = new StockResponse(stock.getId(), stock.getProduct().getCode() ,stock.getProduct().getName(), stock.getPosition().getCode(), stock.getAmount());

        return ResponseEntity.created(uri).body(dto);
    }

    public ResponseEntity<Page<StockResponse>> getAll(Pageable pagination) {
        Page<StockResponse> stock = stockRepository.findAll(pagination).map(StockResponse::new);
        return ResponseEntity.ok(stock);
    }

    public ResponseEntity<StockResponse> createAdjustment(StockAdjustmentRequest data) {
        Stock.isValidAmount(data.newAmount());

        Stock stock = stockRepository.findById(data.id()).orElseThrow(
                () -> new EntityNotFoundException("Stock not found with id: " + data.id())
        );

        stock.updateAmount(data.newAmount());

        MovementStock movementStock = MovementStockMapper.createAjustment(stock.getProduct().getCode(), stock.getProduct().getName(), stock.getPosition().getCode(), data.newAmount(), data.responsible(), data.reason());
        movementStockRepository.save(movementStock);

        StockResponse dto = new StockResponse(stock);

        return ResponseEntity.ok(dto);
    }
}
