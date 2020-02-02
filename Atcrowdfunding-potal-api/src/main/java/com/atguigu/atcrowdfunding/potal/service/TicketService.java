package com.atguigu.atcrowdfunding.potal.service;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Ticket;

/**
 * @author wall
 * @data - 16:02
 */
public interface TicketService {
    Ticket getTicketByMemberId(Integer id);

    void updatePstep(Ticket ticket);

    void saveTicket(Ticket ticket);

    void updatePiidAndPstep(Ticket ticket);

    Member getMemberByPiid(String processInstanceId);

    void updateStatus(Member member);
}
