package bgdn.vrbv.prjct.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacation_sequence_id")
    @SequenceGenerator(name = "vacation_sequence_id",
            allocationSize = 1,
            sequenceName = "vacation_sequence_id")
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Employee employee;


}
