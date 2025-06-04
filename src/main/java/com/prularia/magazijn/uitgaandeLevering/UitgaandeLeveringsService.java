package com.prularia.magazijn.uitgaandeLevering;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UitgaandeLeveringsService {
    private final UitgaandeLeveringsRepository uitgaandeLeveringsRepository;

    public UitgaandeLeveringsService(UitgaandeLeveringsRepository uitgaandeLeveringsRepository) {
        this.uitgaandeLeveringsRepository = uitgaandeLeveringsRepository;
    }

    @Transactional
    public void updateStatusToBeschadigd(long bestelId) {
        uitgaandeLeveringsRepository.updateStatusToBeschadigd(bestelId);
    }
}
