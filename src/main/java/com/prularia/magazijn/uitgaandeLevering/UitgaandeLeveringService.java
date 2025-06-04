package com.prularia.magazijn.uitgaandeLevering;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UitgaandeLeveringService {
    private final UitgaandeLeveringsRepository uitgaandeLeveringsRepository;
    public UitgaandeLeveringService(UitgaandeLeveringsRepository uitgaandeLeveringsRepository) {
        this.uitgaandeLeveringsRepository = uitgaandeLeveringsRepository;
    }

public boolean  heeftStatus6(long bestelId){
        return uitgaandeLeveringsRepository.isStatus6(bestelId);
}


public boolean  heeftStatus4(long bestelId){
        return uitgaandeLeveringsRepository.isStatus4(bestelId);
}

}
