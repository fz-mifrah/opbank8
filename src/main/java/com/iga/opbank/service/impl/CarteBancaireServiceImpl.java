package com.iga.opbank.service.impl;

import com.iga.opbank.domain.CarteBancaire;
import com.iga.opbank.repository.CarteBancaireRepository;
import com.iga.opbank.service.CarteBancaireService;
import com.iga.opbank.service.dto.CarteBancaireDTO;
import com.iga.opbank.service.mapper.CarteBancaireMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CarteBancaire}.
 */
@Service
@Transactional
public class CarteBancaireServiceImpl implements CarteBancaireService {

    private final Logger log = LoggerFactory.getLogger(CarteBancaireServiceImpl.class);

    private final CarteBancaireRepository carteBancaireRepository;

    private final CarteBancaireMapper carteBancaireMapper;

    public CarteBancaireServiceImpl(CarteBancaireRepository carteBancaireRepository, CarteBancaireMapper carteBancaireMapper) {
        this.carteBancaireRepository = carteBancaireRepository;
        this.carteBancaireMapper = carteBancaireMapper;
    }

    @Override
    public CarteBancaireDTO save(CarteBancaireDTO carteBancaireDTO) {
        log.debug("Request to save CarteBancaire : {}", carteBancaireDTO);
        CarteBancaire carteBancaire = carteBancaireMapper.toEntity(carteBancaireDTO);
        carteBancaire = carteBancaireRepository.save(carteBancaire);
        return carteBancaireMapper.toDto(carteBancaire);
    }

    @Override
    public CarteBancaireDTO update(CarteBancaireDTO carteBancaireDTO) {
        log.debug("Request to save CarteBancaire : {}", carteBancaireDTO);
        CarteBancaire carteBancaire = carteBancaireMapper.toEntity(carteBancaireDTO);
        carteBancaire = carteBancaireRepository.save(carteBancaire);
        return carteBancaireMapper.toDto(carteBancaire);
    }

    @Override
    public Optional<CarteBancaireDTO> partialUpdate(CarteBancaireDTO carteBancaireDTO) {
        log.debug("Request to partially update CarteBancaire : {}", carteBancaireDTO);

        return carteBancaireRepository
            .findById(carteBancaireDTO.getId())
            .map(existingCarteBancaire -> {
                carteBancaireMapper.partialUpdate(existingCarteBancaire, carteBancaireDTO);

                return existingCarteBancaire;
            })
            .map(carteBancaireRepository::save)
            .map(carteBancaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarteBancaireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarteBancaires");
        return carteBancaireRepository.findAll(pageable).map(carteBancaireMapper::toDto);
    }

    /**
     *  Get all the carteBancaires where Compte is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CarteBancaireDTO> findAllWhereCompteIsNull() {
        log.debug("Request to get all carteBancaires where Compte is null");
        return StreamSupport
            .stream(carteBancaireRepository.findAll().spliterator(), false)
            .filter(carteBancaire -> carteBancaire.getCompte() == null)
            .map(carteBancaireMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarteBancaireDTO> findOne(Long id) {
        log.debug("Request to get CarteBancaire : {}", id);
        return carteBancaireRepository.findById(id).map(carteBancaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CarteBancaire : {}", id);
        carteBancaireRepository.deleteById(id);
    }
}
