package com.spin.kill.server.service;

import com.spin.kill.server.dto.MailDto;

public interface MailService {
    public void sendSimpleEmail(final MailDto dto);

    public void sendHTMLMail(final MailDto dto);
}
