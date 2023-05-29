# ТЕЛЕГРАМ-БОТ для приюта собак и кошек


## Функционал для пользователя

**Этап 0. Определение запроса** 

*Это входная точка общения бота с пользователем.* 

- Бот приветствует нового пользователя, рассказывает о себе и просит пользователя выбрать приют:
    - Приют для кошек
    - Приют для собак
    
    Выбрать оба приюта нельзя.
    
- Бот может выдать меню на выбор, с каким запросом пришел пользователь:
    - Узнать информацию о приюте (этап 1)
    - Как взять животное из приюта (этап 2)
    - Прислать отчет о питомце (этап 3)
    - Позвать волонтера
- Если ни один из вариантов не подходит, то бот может позвать волонтера.
- Если пользователь уже обращался к боту ранее, то новое обращение начинается с выбора приюта, о котором пользователь хочет узнать.

**Этап 1. Консультация с новым пользователем** 

*На данном этапе бот выдводит вводную информацию о приюте: где он находится, как и когда работает, какие правила пропуска на территорию приюта, правила нахождения внутри и общения с животным. Функционал приюта для кошек и для собак идентичный, но информация внутри будет разной, так как приюты находятся в разном месте и у них разные ограничения и правила нахождения с животными. 
Важно: база данных для пользователей разных приютов должна быть разной.* 

- Бот приветствует пользователя.
- Бот может рассказать о приюте.
- Бот может выдать расписание работы приюта и адрес, схему проезда.
- Бот может выдать контактные данные охраны для оформления пропуска на машину.
- Бот может выдать общие рекомендации о технике безопасности на территории приюта.
- Бот может принять и записать контактные данные для связи.
- Если бот не может ответить на вопросы клиента, то можно позвать волонтера.

**Этап 2. Консультация с потенциальным хозяином животного из приюта** 

*На данном этапе бот помогает потенциальным хозяевам животного из приюта разобраться с бюрократическими (оформление договора) и бытовыми (как подготовиться к жизни с животным) вопросами.* 

- Бот приветствует пользователя.
- Бот может выдать правила знакомства с животным до того, как забрать его из приюта.
- Бот может выдать список документов, необходимых для того, чтобы взять животное из приюта.
- Бот может выдать список рекомендаций по транспортировке животного.
- Бот может выдать список рекомендаций по обустройству дома для щенка/котенка.
- Бот может выдать список рекомендаций по обустройству дома для взрослого животного.
- Бот может выдать список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение).
- Бот может выдать советы кинолога по первичному общению с собакой *(неактуально для кошачьего приюта).*
- Бот может выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним *(неактуально для кошачьего приюта, реализовать только для приюта для собак).*
- Бот может выдать список причин, почему могут отказать и не дать забрать собаку из приюта.
- Бот может принять и записать контактные данные для связи. *(Важно: база данных для пользователей разных приютов разная.)*
- Если бот не может ответить на вопросы клиента, то можно позвать волонтера.

**Этап 3. Ведение питомца** 

*После того как новый хозяин забрал животное из приюта, он обязан в течение месяца присылать информацию о том, как животное чувствует себя на новом месте. В ежедневный отчет входит следующая информация:* 

- *Фото животного.*
- *Рацион животного.*
- *Общее самочувствие и привыкание к новому месту.*
- *Изменение в поведении: отказ от старых привычек, приобретение новых.*

*Отчет нужно присылать каждый день, ограничений в сутках по времени сдачи отчета нет. Каждый день волонтеры отсматривают все присланные отчеты после 21:00. В случае, если новый хозяин не должным образом заполнял отчет, волонтер через бота может дать обратную связь в стандартной форме: «Дорогой хозяин, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного».* 

*В базу новых хозяев пользователь попадает через волонтера, который его туда заносит. Для усыновителей кошачьего приюта база одна, для собачьего приюта — другая.* 

*Задача бота — принимать на вход информацию и в случае, если пользователь не присылает информации, напоминать об этом, а если проходит более 2 дней, то отправлять запрос волонтеру на связь с усыновителем.* 

*Как только период в 30 дней заканчивается, волонтеры принимают решение о том, остается животное у хозяина или нет. Испытательный срок может быть пройден, может быть продлен на срок еще 14 или 30 дней, а может быть не пройден.* 

- Бот может прислать форму ежедневного отчета.
- Если пользователь прислал только фото, то бот может запросить текст.
- Если пользователь прислал только текст, то бот может запросить фото.
- Бот может выдать предупреждение о том, что отчет заполняется плохо (делает волонтер): 
«*Дорогой хозяин, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного».*
- Если новый хозяин прошел испытательный срок, то бот поздравляет его стандартным сообщением.
- Если новому хозяину было назначено дополнительное время испытательного срока, то бот сообщает ему и указывает количество дополнительных дней (14 или 30 дней).
- Если новый хозяин не прошел испытательный срок, то бот уведомляет его об этом и дает инструкции по дальнейшим шагам.
- Если бот не может ответить на вопросы клиента, то можно позвать волонтера.
