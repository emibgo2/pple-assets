package link.pple.assets.domain.donation.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import link.pple.assets.domain.donation.entity.DonationHistory
import link.pple.assets.domain.donation.repository.DonationHistoryRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import strikt.api.expectThat
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import java.util.*

/**
 * @Author Heli
 */
@ExtendWith(MockKExtension::class)
internal class DonationHistoryQueryTest {

    @MockK
    private lateinit var donationHistoryRepository: DonationHistoryRepository

    @InjectMockKs
    private lateinit var sut: DonationHistoryQuery

    @Test
    fun `DonationHistory 목록을 조회할 수 있다`() {
        // given
        val donorAccountUuid = UUID.randomUUID()
        val donationUuid = UUID.randomUUID()
        val firstDonationHistory = DonationHistory.create(
            donation = mockk(),
            donor = mockk()
        )
        val secondDonationHistory = DonationHistory.create(
            donation = mockk(),
            donor = mockk()
        )
        every {
            donationHistoryRepository.findAll(
                donorAccountUuid = donorAccountUuid,
                donationUuid = donationUuid
            )
        } returns listOf(firstDonationHistory, secondDonationHistory)

        // when
        val donationHistories = sut.getAll(
            donorAccountUuid = donorAccountUuid.toString(),
            donationUuid = donationUuid.toString()
        )

        // then
        expectThat(donationHistories) {
            hasSize(2)
            get(0).and {
                get { donation } isNotEqualTo null
                get { donor } isNotEqualTo null
                get { step } isEqualTo DonationHistory.Step.CONNECT
            }
            get(1).and {
                get { donation } isNotEqualTo null
                get { donor } isNotEqualTo null
                get { step } isEqualTo DonationHistory.Step.CONNECT
            }
        }
    }

    @Test
    fun `조회할 DonationHistory 가 없으면 빈 목록을 반환한다`() {
        // given
        val donorAccountUuid = UUID.randomUUID()
        val donationUuid = UUID.randomUUID()
        every {
            donationHistoryRepository.findAll(
                donorAccountUuid = donorAccountUuid,
                donationUuid = donationUuid
            )
        } returns emptyList()

        // when
        val donationHistories = sut.getAll(
            donorAccountUuid = donorAccountUuid.toString(),
            donationUuid = donationUuid.toString()
        )

        // then
        expectThat(donationHistories) hasSize 0
    }
}
