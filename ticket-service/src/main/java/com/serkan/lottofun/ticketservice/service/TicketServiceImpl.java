package com.serkan.lottofun.ticketservice.service;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.common.event.TicketPurchasedEvent;
import com.serkan.lottofun.common.event.TicketWonEvent;
import com.serkan.lottofun.ticketservice.config.RabbitMQConfig;
import com.serkan.lottofun.ticketservice.dto.DrawResponse;
import com.serkan.lottofun.ticketservice.dto.DtoTicketResponse;
import com.serkan.lottofun.ticketservice.dto.UserResponse;
import com.serkan.lottofun.ticketservice.entity.PrizeType;
import com.serkan.lottofun.ticketservice.entity.Ticket;
import com.serkan.lottofun.ticketservice.entity.TicketStatus;
import com.serkan.lottofun.ticketservice.exception.BaseException;
import com.serkan.lottofun.ticketservice.exception.ErrorMessage;
import com.serkan.lottofun.ticketservice.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements ITicketService{
    private final TicketRepository ticketRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public DtoTicketResponse purchaseTicket(String username, Long drawId, String selectedNumbers) {
        String url = "http://draw-service:8081/draws/draw-info/" + drawId;
        String userUrl = "http://auth-service:8080/users/user-info/" + username;
        String authHeader = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        System.out.println("FORWARDING HEADER: " + authHeader);
        ResponseEntity<DrawResponse> drawResponse =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        DrawResponse.class
                );
        System.out.println("DRAW CALL SUCCESS");

        DrawResponse draw = drawResponse.getBody();
        ResponseEntity<UserResponse> userResponse =
                restTemplate.exchange(
                        userUrl,
                        HttpMethod.GET,
                        entity,
                        UserResponse.class
                );
        System.out.println("USER CALL SUCCESS");
        UserResponse user = userResponse.getBody();

        if (user == null) {
            throw new BaseException(ErrorMessage.USER_NOT_FOUND);
        }
        if (draw == null) {
            throw new BaseException(ErrorMessage.DRAW_NOT_FOUND);
        }
        if (draw.getStatus() != DrawStatus.DRAW_OPEN) {
            throw new BaseException(ErrorMessage.DRAW_NOT_OPEN);
        }
        Long userId = user.getId();
        BigDecimal ticketPrice = draw.getTicketPrice();
        validateNumbers(selectedNumbers);
        if (ticketRepository.existsByDrawIdAndSelectedNumbers(
                drawId, selectedNumbers)) {
            throw new BaseException(ErrorMessage.TICKET_SOLD);
        }
        String decreaseUrl =
                "http://auth-service:8080/users/decrease-balance?userId="
                        + userId
                        + "&amount="
                        + ticketPrice;

        restTemplate.postForEntity(decreaseUrl, entity, Void.class);

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setDrawId(drawId);
        ticket.setSelectedNumbers(selectedNumbers);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket.setTicketStatus(TicketStatus.PENDING);
        ticket.setMatchCount(0);
        ticket.setPrizeType(PrizeType.NONE);
        ticket.setTicketNumber(generateTicketNumber(userId));
        ticketRepository.save(ticket);

        TicketPurchasedEvent event = new TicketPurchasedEvent(
                userId,
                drawId,
                ticketPrice
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TICKET_PURCHASED_QUEUE,
                event
        );

        DtoTicketResponse dto = new DtoTicketResponse();
        BeanUtils.copyProperties(ticket, dto);
        return dto;
    }

    @Override
    public List<DtoTicketResponse> getTicketsByUserId(Long userId) {

        List<DtoTicketResponse> dtoTicketResponseList = new ArrayList<>();
        List<Ticket> ticketList = ticketRepository.findByUserId(userId);
        if (ticketList.isEmpty()) {
            throw new BaseException(ErrorMessage.TICKET_NOT_FOUND);
        }
        for (Ticket ticket : ticketList) {
            DtoTicketResponse dtoTicketResponse = new DtoTicketResponse();
            BeanUtils.copyProperties(ticket, dtoTicketResponse);
            dtoTicketResponse.setDrawId(ticket.getDrawId());
            dtoTicketResponse.setUserId(ticket.getUserId());
            dtoTicketResponse.setTicketStatus(ticket.getTicketStatus());
            dtoTicketResponse.setPrizeType(ticket.getPrizeType() != null ? ticket.getPrizeType() : null);
            dtoTicketResponseList.add(dtoTicketResponse);
        }
        return dtoTicketResponseList;
    }

    @Override
    public List<DtoTicketResponse> getTicketsByDrawId(Long drawId) {
        List<DtoTicketResponse> dtoTicketResponseList = new ArrayList<>();
        List<Ticket> ticketList = ticketRepository.findByDrawId(drawId);
        if (ticketList.isEmpty()) {
            throw new BaseException(ErrorMessage.TICKET_NOT_FOUND);
        }
        for (Ticket ticket : ticketList) {
            DtoTicketResponse dtoTicketResponse = new DtoTicketResponse();
            BeanUtils.copyProperties(ticket, dtoTicketResponse);
            dtoTicketResponse.setDrawId(ticket.getDrawId());
            dtoTicketResponse.setUserId(ticket.getUserId());
            dtoTicketResponse.setTicketStatus(ticket.getTicketStatus());
            dtoTicketResponse.setPrizeType(ticket.getPrizeType() != null ? ticket.getPrizeType() : null);
            dtoTicketResponseList.add(dtoTicketResponse);
        }
        return dtoTicketResponseList;
    }

    @Override
    @Transactional
    public void processDrawExtracted(Long drawId, String winningNumbers , BigDecimal jackpotPool) {
        System.out.println("GELEN DRAW ID: " + drawId);
        List<Ticket> tickets = ticketRepository.findByDrawId(drawId);
        System.out.println("BULUNAN TICKET SAYISI: " + tickets.size());
        Set<Integer> winningSet = parseNumbers(winningNumbers);
        for (Ticket ticket : tickets) {
            Set<Integer> ticketSet = parseNumbers(ticket.getSelectedNumbers());
            ticketSet.retainAll(winningSet);
            int matchCount = ticketSet.size();

            PrizeType prizeType = determinePrizeType(matchCount);

            ticket.setMatchCount(matchCount);
            ticket.setPrizeType(prizeType);

            ticket.setPrizeAmount(calculatePrize(prizeType, jackpotPool));
            ticket.setTicketStatus(matchCount >= 2 ? TicketStatus.WON : TicketStatus.LOST);
            if (ticket.getPrizeAmount() != null && ticket.getPrizeAmount().compareTo(BigDecimal.ZERO) > 0) {

                TicketWonEvent wonEvent =
                        new TicketWonEvent(
                                ticket.getUserId(),
                                ticket.getId(),
                                ticket.getPrizeAmount()
                        );

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.TICKET_WON_QUEUE,
                        wonEvent
                );
            }

//            ticketRepository.save(ticket);
        }
    }
    private Set<Integer> parseNumbers(String numbers) {
        if (numbers == null || numbers.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(numbers.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }
    private PrizeType determinePrizeType(int matchCount) {
        return switch (matchCount) {
            case 5 -> PrizeType.JACKPOT;
            case 4 -> PrizeType.HIGH;
            case 3 -> PrizeType.MEDIUM;
            case 2 -> PrizeType.LOW;
            default -> PrizeType.NONE;
        };
    }
    private BigDecimal calculatePrize(PrizeType prizeType, BigDecimal jackpotPool) {
//        double percentage = prizeType.getPercentage();
//        return jackpotPool.multiply(BigDecimal.valueOf(percentage / 100));
        return jackpotPool
                .multiply(BigDecimal.valueOf(prizeType.getPercentage()))
                .divide(BigDecimal.valueOf(100));
    }
    private void validateNumbers(String selectedNumbers) {
        if (selectedNumbers == null || selectedNumbers.isBlank()) {
            throw new BaseException(ErrorMessage.NOT_EXIST_NUMBER);
        }
        String[] parts = selectedNumbers.split(",");

        if (parts.length != 5) {
            throw new BaseException(ErrorMessage.INVALID_NUMBER_COUNT);
        }

        Set<Integer> uniqueNumbers = new HashSet<>();
        for (String part : parts) {
            int num = Integer.parseInt(part.trim());
            if (num < 1 || num > 49) {
                throw new BaseException(ErrorMessage.INVALID_NUMBER_RANGE);
            }
            if (!uniqueNumbers.add(num)) {
                throw new BaseException(ErrorMessage.DUPLICATE_NUMBERS);
            }
        }
    }
    private String generateTicketNumber(Long userId) {
        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("USR%d-%s-%s", userId, date, randomPart);
    }
}
